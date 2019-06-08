package com.usaa.support.cacheutil;

import org.apache.commons.cli.*;

public class Arguments {

    private String xmlFile = "hazelcast-client.xml";
    private String cacheName = null;
    private Object key = null;
    private Object value = null;
    private Action action = Action.get;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(String xmlFile) {
        this.xmlFile = xmlFile;
    }

    @Override
    public String toString() {
        return "Arguments{" +
                "xmlFile='" + xmlFile + '\'' +
                ", cacheName='" + cacheName + '\'' +
                ", key=" + key +
                ", value=" + value +
                ", action=" + action +
                '}';
    }

    public static Options createOptions()
    {
        /// create the Options
        Options options = new Options();
        options.addOption("x", "xml-file", true, "The hazelcast client config xml-file." );
        options.addOption("n", "cache-name", true, "The cache name" );

        options.addOption("k", "key", true, "The key" );
        options.addOption("v", "value", true, "The value" );

        options.addOption("h", "help", false, "help" );

        options.addOption("a", "action", true,
                "Action to take: \n" +
                        "'get' <key> from <cache-name>\n" +
                        "'put' <key> the <value> into <cache-name>\n" +
                        "'getAll' <cache-name>\n" +
                        "'create' <cache-name>\n" +
                        "'destroy' <cache-name>\n" +
                        "'remove' <key> from <cache-name>" );

        return options;
    }

    public static Arguments createArguments(String[] args)
    {
        Arguments arguments = new Arguments();
        Options options = Arguments.createOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;

        try {
            line = parser.parse( options, args );

            if( line.hasOption( "help" ) ) {
                arguments.setAction(Action.usage);
                return arguments;
            }

//            if( line.hasOption( "action" ) ) {
            arguments.setAction(Action.valueOf(line.getOptionValue("action")));
//            }

//            if( line.hasOption( "xml-file" ) ) {
                arguments.setXmlFile(line.getOptionValue("xml-file"));
//            }
//            if( line.hasOption( "cache-name" ) ) {
                arguments.setCacheName(line.getOptionValue("cache-name"));
//            }

            switch (arguments.getAction()) {
                case get:
                case remove:
                    arguments.setKey(line.getOptionValue("key"));
                    break;

                case put:
                    arguments.setKey(line.getOptionValue("key"));
                    arguments.setValue(line.getOptionValue("value"));
                    break;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return arguments;
    }

}
