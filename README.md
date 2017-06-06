# Nuxeo jBPM

This addon provides an interface to manage workflows through the jBPM engine.

It was included by default up to Nuxeo 5.5, but since Nuxeo 5.6 it has been deprecated and moved
to a separate addon, and replaced by the new Document Routing module.

This addon works with Nuxeo 9.2


## Building and deploying

### How to build

You can build this module with:

    $ mvn clean package -DskipTests

### How to deploy

#### Deploy the module

Inside target directory of nuxeo-jbpm-package module you will find a zip file which is a regular Nuxeo package and can be installed via nuxeoctl tool.

#### Configure the Datasource

Nuxeo jBPM relies on a Datasource `nxjbpm` which you may need to define in your server's context.xml

## About Nuxeo

Nuxeo provides a modular, extensible Java-based [open source software platform for enterprise content management] [1] and packaged applications for [document management] [2], [digital asset management] [3] and [case management] [4]. Designed by developers for developers, the Nuxeo platform offers a modern architecture, a powerful plug-in model and extensive packaging capabilities for building content applications.

[1]: http://www.nuxeo.com/en/products/ep
[2]: http://www.nuxeo.com/en/products/document-management
[3]: http://www.nuxeo.com/en/products/dam
[4]: http://www.nuxeo.com/en/products/case-management

More information on: <http://www.nuxeo.com/>


