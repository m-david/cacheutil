package com.usaa.support.cacheutil;

import org.apache.commons.cli.*;

public class Arguments {

    private String xmlFile = "hazelcast-client.xml";
    private String cacheName = null;
    private String key = null;
    private Action action = Action.get;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
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
                ", key='" + key + '\'' +
                ", action=" + action +
                '}';
    }

    public static Options createOptions()
    {
        /// create the Options
        Options options = new Options();
        options.addOption("x", "xml-file", true, "the hazelcast client config xml-file." );
        options.addOption("n", "cache-name", true, "the cache name" );
        options.addOption("k", "key", true, "the key name" );
        options.addOption("a", "action", true, "the action to take" );

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

            if( line.hasOption( "xml-file" ) ) {
                arguments.setXmlFile(line.getOptionValue("xml-file"));
            }
            if( line.hasOption( "cache-name" ) ) {
                arguments.setCacheName(line.getOptionValue("cache-name"));
            }
            if( line.hasOption( "key" ) ) {
                arguments.setKey(line.getOptionValue("key"));
            }
            if( line.hasOption( "action" ) ) {
                arguments.setAction(Action.valueOf(line.getOptionValue("action")));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return arguments;
    }

}
