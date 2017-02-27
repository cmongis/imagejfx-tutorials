# All you need to know to work on ImageJ FX (UI Edition)
This document contains a set of resources and knowledge required to become an efficient UI Programmer for ImageJFX. Basic knowledge of Java Programming is requested in order to complete this guide. Multiple exercises are proposed and should be executed following [Java Programming style conventions](http://geosoft.no/development/javastyle.html).

By **Cyril MONGIS**, 2016



## The tools used in the project

During ImageJFX development, you will have to use three main tools : Netbeans for development, Maven to deal with the package dependancies, and Git hub for source code synchronization.

### 1. Downlowd Netbeans

Netbean is now the main editor used to work on ImageJFX. Download it and install it. Netbeans includes plugins that offer Maven and Git related actions directly the software. It becomes then your main working environment.

### Maven

Maven takes care of dependancy management for Java projects. In the old times, developers would browse the internet and download manually the JAR libraries that they would need for their project. When sharing the code of the projects, the developer had to either include the JAR he used, or let the other developers the task to seek for themselves the missing dependancies. Maven avoids all this complication by taking care of indexing and and dowloading all the dependancies associated to the project. It connects itself to a database that indexes many important libraries. The developer adds the informations of the library required for the project while Maven takes care of downloading them.
This functionnality eases code sharing for two main reasons. First the size of the files to share is smalled since only a text file containing the list of dependancies is necessary. Second, new comers dont' need to browse the whole internet in order to find the corresponding dependancies because Maven will download them automatically.

#### (〜￣▽￣)〜 Exercise 1

Create a Maven Java Application project with Netbeans and add the libraries **ControlsFX** and **scijava-common** as dependancy.

### Github

Github is a famous git platform host git projects. Git allows developers to share code modifications and synchronize them efficiently.

For Github introduction, the internet is full of them. I would advice you to have a deep understanding of the concepts **commit**, **branch** and **checkout**. You should be able to tell exactly what happen during each of these processes. A lot of time can be lost because of misunderstanding of the Git machinery, making he team spend more time trying to recover lost changes and solve conflicts.

#### (〜￣▽￣)〜 Exercise 2.1

Create a Github account and a repository called **imagejfx-training**. You will commit the exercises there so I can evaluate them. Commit your maven project to this repository using Netbeans.

### DCEVM

DCEVM is a debugging virtual machine. You can launch a app and modify the code as the application runs. When you save the modification, the code of the running app is automatically updated, preventing you to relaunch the app to see the modifications of the code.

#### (〜￣▽￣)〜 Exercise 2.2

Download and install DCEVM on your computer as **altvm**. Make sure Netbeans can use it.

## Java basics

### Functional interfaces, Lambdas and method referencing

Functional interfaces, Lambdas and Method Referencing are new features introduced in Java 8 and are fairly used in the project.

There is a fair amount of literature over Functional interfaces. You should first get a grasp of the concept of Functional Interface :

 - [JavaZone tutorial](https://dzone.com/articles/introduction-functional-1)
 - [O'Reilly's radar](http://radar.oreilly.com/2014/08/java-8-functional-interfaces.html)
 - [An overview of Method References](https://dzone.com/articles/methodreference)

But most of the time, the 4 following functionnaly interfaces **Runnable**, **Callable**, **Consumer** and **Callback** are enough in most of the project. Very few functionnaly interfaces where created in the project.

One of the main advantage of Functional Interfaces is that it can be used in pair with method referencing. A method that follows the requirement of a the functional interface can be passed as parameters instead of a lambda or an anonymous object.

For instance, changes of JavaFX Properties can be listened (Observer Pattern) using a **ChangeListener** which is a Functional interface.

The classic code to add a listener to a property would be :

~~~java
public class MyClass() {
	
	private Property<String> nameProperty = ...
	
	// constructor
	public MyClass() {
	
		// creating an object that implements the functionnal interface ChangeListener
		property.addChangeListener(new ChangeListener<String>() {
			public void changed(Observable obs, String oldValue, String newValue) {
				// display the new value at each changes
				System.out.println(newValue);
			}
		});
		
	}
}
~~~

A lamda version of this code could be :

~~~java
public class MyClass() {
	
	private Property<String> nameProperty = ...
	
	// constructor
	public MyClass() {
	
		// creating an object that implements the functionnal interface ChangeListener
		property.addChangeListener(
			(obs, oldValue,newValue)-> {
				// display the new value at each changes
				System.out.println(newValue);
			});
		
	}
}
~~~
As you can see the lambda must specify all the parameters required for the functionnal interface.

When using lambdas, instead of creating a anonymous class, the listener instanciated by refering to the method :

~~~java
public class MyClass() {

	Property<String> nameProperty = new SimpleStringProperty();

	// constructor
	public MyClass() {
		// method reference
		nameProperty.addListener(this::onNameChanged);
	}
	
	public void onNameChanged(Observable, String oldValue, String newValue) {
		// same process
	}
}
~~~
Note that it would also be possible to write : 

~~~java

ChangeListener<String> changeListener = this::onNameChanged;
nameProperty.addListener(changeListener);


~~~

Method Referencing is quite useful for live debugging because methods can be updated in live using DCEVM, which is not the case with lambdas. Thus, it keeps the code a bit cleaner.

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

#### (〜￣▽￣)〜 Excercise 2.3

Create a class FunctionnalTask that take a functionnal interface as input and execute it in the **call()** method.

~~~java
public class Main() {

	// main method to test the class
	public static void main(String... args) throws Exception {
	
		FunctionnalTask<String> task = new FunctionnalTask(this::getName);
		new Thread(task).start();
		System.out.println("Waiting for it");
		String name = task.get();
		
	}
	
	// referenced method
	public static String getName() throws Exception {
		Threads.sleep(3000);
		return "My name is";
	}
~~~

## JavaFX and FXML

There is plenty of tutorial available for FXML. Follow the following tutorial :

- [javafxtutorials.com](http://www.javafxtutorials.com/)
- [Official Oracle documentation](https://docs.oracle.com/javase/8/javafx/fxml-tutorial/)


### Exercise 3

Create a TODO list JavaFX app. The UI is composed of

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
 - Put paddings inside your UI to let it breath
 - Search for some UX Design / UI Design articles

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

The PluginService allows you to manage your own plugin.

### Create your own plugin

Creating your own plugin is quite easy. You first create a interface extending **SciJavaPlugin**, then create implementation of this interface which you can load using the plugin service.

Let's say we want to propose plugins that act on String object.

First we specify the behaviour of a plugin through an interface :

~~~java 
public interface TextPlugin extends SciJavaPlugin {
	public String processText(String input);
}
~~~

Then, we can start creating text processing plugins. For that, we must create a class that implements **TextPlugin** and has the annotation **@Plugin** showing the **TextPlugin.class** type :

~~~java
@Plugin( type=TextPlugin.class, label = "As upper case")
public class CapitalizeText implements TextPlugin {
	
	@Parameter
	OtherService otherService; // services are injected automatically when instanciating a plugin with the plugin service;
	
	// put the text on upper case
	public void processText(String input) {
		return input.toUpperCase();
	}
}
~~~

**Note** : the @Plugin annotation also proposes you to specify name and labels that can be parsed and used for Ui purpose.

Now we want our Ui to load text processing plugin and propose them as buttons.

~~~java
public TextProcessingToolBar extends ToolBar {
	
	@Parameter
	PluginService pluginService;
	
	// We force the presence of the SciJava Context
	// when creating the tool bar.
	public TextProcessingToolBox(Context context) {
		
		// the context is injected by the toolbox itself
		context.inject(this);
		
		pluginService
		.getPluginsOfType(TextPlugin.class)
		.forEach(this::addPlugin);
		
	}
	
	public void addPlugin(PluginInfo<TextPlugin> pluginInfo) {
		// A toolbar is just a list of buttons.
		// We create a button for each plugin.
		
		//the label of the button is taken from the plugin info object returned by the plugin service
		Button button = new Button(pluginInfo.getLabel());
		
		// an instance of the plugin is created from the plugin info object
		TextPlugin plugin = pluginInfo.createInstance();
		
		// when clicking on the button,
		// the method apply Plugin is called
		button.setOnAction(actionEvent->applyPlugin(plugin));
		
		// we add the button to the tool bar
		getItems().add(button);
	}
	
	public void applyPlugin(TextPlugin plugin) {
		
		
		// The plugin is already loaded.
		// method that apply the plugin on the view or model or wherever
		
		...
		
		String input = view.getCurrentText();
		view.setResult(plugin.processText(input));
		
		...
	}
}
~~~

#### Exercise 5

Modify your TODO App with the following goals in mind : 

 - the "Set all task done" button and "Remove" button are just plugins.
 - Add a two other plugins that allows you to save or load tasks into JSON.
 - The plugins should never touch any element of your controller, but should only interact with your SciJavaService managing your model (I remind you that the **PluginService** injects the SciJavaServices inside the **Plugin** after instanciating them).
 - The model shouldn't hold any JavaFX related object.
 
 **Tips**
 - create a **View-Model** (a model for the view). It's a model inside the controller that mimics the changes of the real model but holds JavaFX specific elements. (The Wrapper technic was a kind of View-Model.
 - Use JSON Jackson librariy 2.4 with data-binding
 - You can try to create a implementation of Property<Boolean> that holds a pointer to your SciJavaService and execute action directly on it.

 
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


