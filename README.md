[![Build Status](https://travis-ci.org/saymagic/MConfig.svg?branch=master)](https://travis-ci.org/saymagic/MConfig)

## Introduction

`MConfig` is a flexible and practical library used for getting configuration information for the application.

## Features
* Multiple Source Suppport:
	* Server
	* File
	* Class's static filed, such as `BuildField` in Android project.
	* Other channels with your own logic
	
* Multiple Data Structure Suppport:
	* Map
	* Property
	* Other structures with your own logic
	
* Custom Piority

## Getting started

### Base Java Lib
In your `build.gradle` 

	dependencies {
	   compile 'tech.saymagic:mconfig-base:1.0.1'
	} 
	
### Android Feature Support

In your `build.gradle` 

	dependencies {
	       compile 'tech.saymagic:mconfig-android:1.0.1'
	} 
	
## Example Usage

Suppose we have three configuration source :

* Default Config: written in source code.
* Remote Config: used for updating default config, fetch from server.
* File Config: used for debug. fetch form local file.
	
We can init MConfig like this:
	
	//Default config, piority is 0.
	Map<String, String> defaultMap = new HashMap<String, String>(){{
            put("first", "Mercury");
            put("second", "Venus");
            //...
        }};
    IConfigProvider<String, String> defaultProvider = new MapConfigProvider<String, String>(defaultMap, 0);

	//Remote config, piority is 1.
    MapConfigProvider<String, String> serverProvider = HttpConfigProvider.builder()
                .setMapParser(new PropertiesParser())
                .setUrl("Your Server Path")
                .setExector(MConfig.DEFAULT_EXECTOR)
                .setPriority(1).build();
        
    //File config, piority is 2.
    File file = new File("Your file path");
    MapConfigProvider<String, String> fileProvider = FileConfigProvider.builder()
                .setMapParser(new PropertiesParser())
                .setSource(file)
                .setPriority(2).build();
                
    //Init MConfig
    MConfig<String, String> config = new MConfig<>();
        config.init(defaultProvider, serverProvider, fileProvider);
        
Completing the above logic, you can use method `config.getProperty("***")` to get the value you want. `MConfig` will select the appropriate value according to the piority.


## Authors and Contributors
The author is [saymagic](https://blog.saymagic.tech)

## Support or Contact
If you have any problems with the library, or want to help, please contact me: saymagic.dev#gmail.com and I will try to reply as soon as I can.

## License

Copyright 2017 saymagic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
                
                
        
    
    


