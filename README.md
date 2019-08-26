# K-Octopus Designer 
## Common info and requirements
Octopus platform is built on a set of processors. Processors can be organized in the Model to perform processing workflow. There are four distinguish types of processors:
1. **Source processors**, that allow Octopus model to connect to multiple data source, that can include Relational Databases, NoSQL databases, distributed data storage like Hadoop, File systems, Streaming data, media file and so on;
2. **Data transformation processors**. This kind of processors perform actual data processing. They can do this by themselves or by delegating data processing to the external general purpose computational engines like Spark, Flink, Hadoop and so on;
3. **Sink processors**. This kind of processors support output of processed data. In some respect Sink processors are mirroring the Source processors and basically should be able to output data into all supported by Source processors data storage's and feeds;
4. **Pipeline processors**. Those processors provide support for simple Processing flows by orchestrating a sequential execution of multiple Models in the specified order. Each pipeline processor ia a wrapper arround Model, so graph diagram it would be the same "processor based" execution graph. This is correct, Octopus provides support of workflows built on Processing models. So, potentially is it possible to run multilayered executioin graph. Orchedtration of such complex workflows is a real chalange, but potentially this is possible.

All processors implement the same Java Interface and therefore formally they are equal. This quality of processor is very important one because it allows us to build Octopus as a highly standardized and homogeneous system that is easy to build and to maintain.

The other part of the Octopus foundation is a set of standard protocols that define all aspects of interactions between Octopus modules and subsystems.
There are two major types of such protocols:
* External protocols, that define interactions with external systems and data sources;
* Internal protocols that conduct intermodal communications.

Processors are not communicating directly to each other, instead they are using intermediate transport, that supports moving data from one processor to another. In this demo we are using Redis streams as a way to move data from one processor to another. So, the Redis standalone v.5+ should be part of demo installation.

There are four K-Octopus projects/modules that are ready for demo so far (the other ones are in progress):
1. **K-Octopus Core**;
2. **K-Octopus Compute** - this module responsible for executing processing models;
3. **K-Octopus Repo** - is placeholder for the Octopus repository. Right now it does almost nothing, just holding list of processor instances to serve K-Octopus Designer Palettes;
4. **K-Octopus Designer** is a graphical, drag-and-drop tool to visually build an Octopus Model Execution Graph.

There are direct inter-dependencies between these project in the order from 1 to 4. So, building of those projects should follow the same order, starting from K-Octopus Core and ending with K-Octopus Designer. 

All projects require Java 8. This limitation is due to commercial software that we are using in Designer (http://www.jidesoft.com/). The version 3.4.7 that we are using is not compatible with Java 9+.

## K-Octopus Designer installation
1. Be sure that all three projects:
  * **K-Octopus Core**;
  * **K-Octopus Compute**;
  * **K-Octopus Repo** -

are installed already.

It means that you have a parent directory for all projects (ex. octopus) and three sub-directories:
* octopus/k-octopus-core;
* octopus/k-octopus-compute;
* octopus/k-octopus-repo.

Clone k-octopus-designer repository to octopus directory

> $ git clone https://github.com/a-matrixlab/k-octopus-designer.git


The core code of K-Octopus Designer was developed in 2012-2013. A lot of things changed since that time and some libraries are not available on maven repository, or hard to find. To resolve this issue we are providing a script to manually install all such artifacts.

> $ k-octopus-designer/maven-inst.sh

If you do not want to pollute your local maven repository, you can go to Dropbox and download jar file:

https://www.dropbox.com/preview/Octopus-Designer/octopus-designer-0.7.1-jar-with-dependencies.jar

## Running K-Octopus Designer

The main purpose of K-Octopus Designer is to build Processing Model Execution Graph. Graph representation is a Json file and you can run it even from Postman.

We would recommend to start from installation on single laptop. After you run Designer, go to File menu and select "Open..." option. You'll get dialog box that you can use to navigate to 

> k-octopus-designer/src/main/resources/model_json_files/SMAmodel_local.json

When you'll open it you'll get following layout.

![K-Octopus Designer](https://github.com/a-matrixlab/k-octopus-designer/blob/master/Screenshot%20from%202019-08-25%2015-01-44.png)

There are three very important **properties** (parameters) that define Execution environment:
1. **Transport Url** - this is Redis Url. In our case we are using local standalone Redis;
2. **Service Url** - this is Url where we are running k-octopus-compute. If this parameter is blank, Designer will use embedded compute engine;
3. **Model Lucene Index** - when you are saving Processing model Graph, you are putting it into Lucene index for future search and analysis.

You should compile created graph by clicking forth from the left icon. The generated Graph Json will appear in the Output window.

So, you can grab this Json and run it from Postman.

Before doing this, please run k-octopus-compute from "octopus/k-octopus-compute/target" directory:

> $ java -jar k-octopus-compute-0.7.1-jar-with-dependencies.jar

![K-Octopus Designer](https://github.com/a-matrixlab/k-octopus-designer/blob/master/Screenshot%20from%202019-08-25%2017-15-49.png)

## Steps to build new Model

The following is a step by step instructions how to build and to run a simplest Octopus model. Before staeting click on right left icon on the Control Bar (looks like a paper list). It will create new canvas for new Model.

**Step 1.** Go to the Octopus palette panel and select "Sources" tab. In the "Sources" tab choose "Test Data Source for Redis" source processor and drag it to the canvas.

**Step 2.** Select "Sinks" tab in the Palette panel and then "Console for Redis" sink processor on it. Drag and drop it on the canvas. Now you have two processors on the canvas and you can connect them.

**Step 3.** Each processor can have inputs and a single output. Source processors have only a single output, sink processors can have multiple inputs, the other processors can have multiple inputs and a single output each. All processors can have an unlimited list of parameters that can be used to specify inputs and an output for each processor. **To connect processors the output of one processor should be connected to the input of the other processor. In our case we have only two processors, so we are going to connect output of "Test Data source for Redis" processor to the input of "Console" sink processor. Hold Ctrl button and drag mouse from output of "Test Data source for Redis" to the input of "Console for Redis for Redis" sink.**

**Step 4.** Now when we've connected our processors we may want to specify parameters for each of them. Lets start with source processor. "Test Data source" is very simple source processor and it has only one parameter "Number of events" with default value equal 10. We can leave it as is or change it.

**Step 5.** Just to remind you, Octopus source processors provide data for the model in a form of events. The event definition is the same as an event in Complex Even Processing (CEP) - a collection of "name-value" pairs witch we call attributes. Source processor supplies events to the model one by one when they are getting available. We have to define each attribute of an event by providing attribute name and attribute value type. 
Click on attribute text box and you'll get an access to the dialog window that Octopus Designer offers to help you to do this work. 

**Step 6.** Next lets move to the sink processor and specify parameters for the "Console for Redis" sink processor. In the "Show Attributes" parameter we should provide the attribute name that we are going to show in output window (Console window) of Octopus Designer.

**Step 7.** Now we can compile model (icon 4 on Control Bar). During compilation Octopus Designer generates a detailed description of the model in JSON format. You can see the result of compilation in Output window of designer.

**Step 8.** Finally we can run model by pushing green dot icon - the most right icon on the Octopus designer  top tool bar. The results of model execution will be in Output window.

**Step 9.** You can save model in your local file system. The "Save" operation performs two actions
1. It saves JSON file;
2. It adds this JSON file to Lucene Index for future use in search and analysis.

