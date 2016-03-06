# All you need to know to work on ImageJ FX (UI Edition)
This document contains a set of resources and knowledge required to become an efficient UI Programmer for ImageJFX. Basic knowledge of Java Programming is requested in order to complete this guide. Multiple exercises are proposed and should be executed following [Java Programming style conventions](http://geosoft.no/development/javastyle.html).

By **Cyril MONGIS**, 2016



## Mastering the project tools

### Netbeans

Netbean is now the main editor used to edit ImageJFX. Download it and install it.

### Maven

Maven is the dependancy management software. It's also included in Netbeans. It deals with Java dependancies avoiding the developer to download them manually. It's pretty useful for sharing your work because new comers just have to get the project file and Maven will download all the dependancies for them.

#### Exercise 1

Create a Maven Java Application project with Netbeans and add the libraries ControlsFX and scijava-common as dependancy.

### Github

Github allows us to share code modification among a group of people. It's fairly used in the project.

For Github introduction, the internet is full of them. I would advice you to have a deep understanding of the concepts **commit**, **branch** and **checkout**. You should be able to tell exactly what happen during each of these processes. A lot of time can be lost because of misunderstanding of the Git machinery, making he team spend more time trying to recover lost changes and solve conflicts.

#### Exercise 2

Create a Github account and a repository called **imagejfx-training**. You will commit the exercises there so I can evaluate them. Commit your maven project to this repository using Netbeans.

## Java basics

### Functional interfaces and Lambdas

Functional interface are a new feature of Java 8 and are fairly used in the project.

There is a fair amount of literature over Functional interfaces. You should first get a grasp of the concept of Functional Interface :

 - [JavaZone tutorial](https://dzone.com/articles/introduction-functional-1)
 - [O'Reilly's radar](http://radar.oreilly.com/2014/08/java-8-functional-interfaces.html)

But most of the time, the 4 FI **Runnable**, **Callable**, **Consumer** and **Callback** are enough in most of the project. 

But one of the main advantage of Functional Interfaces is that a method following the functional interface requirement can be passed as parameters instead of an object implementing this interface.

For instance, JavaFX Properties can have **ChangeListener** which are Functional interface.

The classic code to add a listener to a property would be :

~~~java

Property<String> nameProperty = ...

property.addChangeListener(new ChangeListener<String>() {

	public void changed(Observable obs, String oldValue, String newValue) {
		
		// do some process wit the changed values
	
	}
});

~~~

Thanks to Java 8, the listener can be a method of the class :

~~~java
public class MyClass() {

	Property<String> nameProperty = new SimpleStringProperty();

	// initializing
	public MyClass() {
		nameProperty.addListener(this::onNameChanged);
	}
	
	public void onNameChanged(Observable, String oldValue, String newValue) {
		// same process
	}
}
~~~

You'll see later that it's quite useful in debugging mode and keeps the code well organized inside the controller.

### Streams
Streams are pretty useful to go through list of event and execute parallel processing.

I would advice you to master the simple forEach, map and filter.

 - [Java 8 Streams in 5 minutes](https://blog.idrsolutions.com/2014/11/java-8-streams-explained-5-minutes/)

### Tasks

When executing long processes, it's better to execute them in a separate thread because might in the UI thread will likely block the UI. However, the UI might need to be updated after the process and this should happen in the UI thread. Task gives a base for handling such operations. It also provide a Progress and Status property which can be bound to progress bars and label.

~~~java

Task<Boolean> areAllFilesDirectory = new Task<Boolean>() {
	public Boolean call() {
		File f = new File("./");
		boolean flag = true;
		int numFiles = f.listFiles().size();
		int i = 0;
		for(File subFile : f.listFiles()) {
		
			//simple counter
			i++;
			
			// updating the progress property
			setProgress(i,numFiles);
		
			// updatingin the status property
			setStatus("Scanning "+subFile.getName());
			
			// checking if the file is a directory
			if(subFile.isDirectory() == false) {
				flag = false;
				break;
			}
		}
		return flag;
	}
};

// when we define the process,
// we can add an event that will be executed when the task is over.

areAllFilesDirecotories.setOnSucceed(event->{

	// any UI specific operation

});

new Thread(areAllFilesDirectories).start();

~~~


## JavaFX and FXML

There is plenty of tutorial available for FXML. Follow the following tutorial :

- [javafxtutorials.com](http://www.javafxtutorials.com/)
- [Official Oracle documentation](https://docs.oracle.com/javase/8/javafx/fxml-tutorial/)


### Exercise 3

Create a TODO list javafx app. The UI is composed of

- **ListView** that displays the task
- a **TextField** allowing to enter new task
- A **Button** that set all the tasks as done
- A **Button** to delete the selected task

Inside the list, each element displays the task text and a button to set the task done.

Publish it on your imagejfx-training github account. 

**Advices**

 - Create a complete diagram displaying the different interactions and consequences of the interactions on the model and on the interface (stay far from any implementation details)
 - Create a separate class that contains the whole the model
 - Use interfaces to specify the model
 - Put padding inside your interface to let it breath

## SciJava

SciJava is a scientific platform allowing you to deal with plugins and dependancy injection.

### How it works

SciJava is initialized and creates a **Context** object. When, this object is created, SciJava scans the code searching for **SciJavaServices** and create instances of one of them automatically. SciJava can now inject previously created service to any other object.

In practice, it allows several objects to share the same instance of a service. It also avoid to pass every service to each object when instanciating them. One other advantage is that instances of a Service can be swapped inside the Context using a priority system.

### Create a custom Service


Services should be specified using a **Interface** and its default implementation. Objects that want to use the service should request injection of the inteface while SciJava will select which implementation to inject.

Let's create a Service that will hold some data. First we specify the Service behaviour with an interface that extends **SciJavaService**

~~~java
public interface CustomDataService extends SciJavaService{
	
	void addData(Data data);
	List<Data> getDataList();
	void removeData(Data data);
}
~~~

Then, we create an implementation of the **CustomDataService** with the annotation  **@Plugin** so SciJava loads it automatically.

~~~java
@Plugin(type = SciJavaService.class,priority = 10)
public class DefaultCustomDataService extends AbstractService implements CustomDataService {
	List<Data> dataList = ...
	
	public void addData(Data data) {
		dataList.add(data);
	}
	...
	List<Data> getDataList() {
		return dataList;
	}
	void removeData(Data data) { ...}
}
~~~

Adding the Annotation **@Plugin** will cause  **SciJava** to load and instanciate the service automatically. The **priority** variable specifies which service should be instantiated first. It can be used to make sure some services are instanciated before other ones. Thus, if there is two implementations of a same service, SciJava will inject the implementation with the highest priortiy, which can be quite useful if someone wants to override an implementation of a particular service in the whole application. 

Now we want to use our **CustomDataService** inside the Ui Object in addition of other services. When declaring our class, we add the name of the **interface** along with the annotation @Parameter so the context object knows which type service to inject : 

~~~java
public class UiMain {
	
	@Parameter
	CustomDataService dataService;

	@Parameter
	OtherService otherService;
	
	...

}
~~~


~~~java
public static void main(String... args) {
	
	SciJava context = new SciJava();
	UiMain uiMain = new UiMain();
	
	// when injecting the context,
	// all the attributes annoted with
	// @Parameter will be pointed
	// to the corresponding service
	scijava.context().inject(uiMain);

	
}

~~~
As you can see, the **CustomDataService** and the **OtherService** where not passed as parameter when constructing the UiMain but *injected* by the SciJava. It is quite useful for ui elements that deal with a lot of different services.


### Important services

#### EventService

The EventService allows you to create you own events and have automatic Event Binding. Let's create an event for our DataService :

~~~java
public class DataAddedEvent extends SciJavaEvent{
	public final Data data;
	public DataAddedEvent(Data data) {
		this.data  = data;
	}
	public Data getData() {
		return this.data;
	}
}
~~~
Now that our event is created, we can change our **UiMain** so it's notified each time a **Data** is added to the **DataService**.

First we modify our implementation of **DataService** so it can emit events.

~~~java
@Plugin(type = SciJavaService.class,priority = 10)
public class DefaultDataService extends AbstractService implements DataService {
	List<Data> dataList = ...
	
	@Parameter
	EventService eventService;
	
	public void addData(Data data) {
		dataList.add(data);
		
		// now the event service will notify all
		// injected object that listen for DataEventEvents
		eventService.publish(new DataAddedEvent(data));
	}
	...
	List<Data> getDataList() {
		return dataList;
	}
	void removeData(Data data) { ...}
}
~~~

Now we make our **UiMain** listen for **DataAddedEvent**. To do so, one just need to add the annotation **@EventHandler** to a method that accept as parameter a **SciJavaEvent**.

~~~java
public class UiMain {
	
	@Parameter
	DataService dataService;

	@Parameter
	OtherService otherService;
	
	...
	
	@FXML
	ListView<Data> listView;
	
	
	@EventHandler
	public onDataEventAdded(DataAddedEvent event) {
		listView.getItems().add(data);
	}
}
~~~

Voila ! The main advantage of this procedure is that now, several UI can listen use the same service and listen to the same events by only using a common entry point : the **Context**.

#### Excercise 4


Create a new branch of your repository and switch to it. Modify your TODO app in a way that the model is inside a SciJava service. The service should be injected using a **Context** element. The service should also already contain a certain number of data. It means the view will load the already containted data to it. Modification of the model should be handle via SciJavaEvents.


#### PluginService


### Create your own plugin





## Structure of ImageJ FX
ImageJ FX is made of Activities, UiPlugin, and Services.

### Services

Services are the most important part of the work. They hold the data and the data logic. They also allow different activities to get access to the  same data and modify them the same way.

### Activities

Activities are UI Element displayed in the center screen of ImageJ FX. When created, Activities are automatically injected with the Services and can populate the UI with informations inside the service.

### UiPlugin

UiPlugin allows you to display panels on the side of the screen in specific contexts to help the activities. To know more about context, please refer to the developer section of ImageJFX.


## Activity

### Description


### Creating an activity


## Important ImageJ/SciJava Services


### EventService

### PluginService

### 


# Personal Database specificities

## What's a project
A project is an interface that contains planes. To each plane is associated a set of metadata.

## Project Cards


