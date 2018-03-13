package com.demo.dao.mongo.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
	@Autowired
	private void setupConfig(
			@Value("${mongo.version}") int version,
			@Value("${mongo.databasename}") String databaseName,
			@Value("${mongo.connentstring}") String connentString) {
		Config.VERSION = version;
		Config.DATABASENAME = databaseName;
		Config.CONNENTSTRING = connentString;
	}

	public static class Config {

		public static int VERSION;
		public static String DATABASENAME;
		public static String CONNENTSTRING;
	}

	/*
	 * Mongo
	 */
	@Autowired
	private void setupTableName(
			@Value("${mongo.msg}") String message,
			@Value("${mongo.other1}") String other1,
			@Value("${mongo.other2}") String other2) {
		TableName.MSG_TABLE = message;
		TableName.OTHER1 = other1;
		TableName.OTHER2 = other2;
	}

	public static class TableName {
		public static String MSG_TABLE = "msg_table";
		public static String OTHER1 = "other1_table";
		public static String OTHER2 = "other2_table";
	}
}
