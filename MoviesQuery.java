// Copyright 2012-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// Licensed under the Apache License, Version 2.0.

package com.amazonaws.samples;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

public class MoviesQuery {

    public static void main(String[] args) throws Exception {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://dynamodb.us-west-2.amazonaws.com", "us-west-2"))
            .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Rating");

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#yr", "userId");
        int y = 50;
        System.setOut(new PrintStream(new FileOutputStream("ratings_set1.dat")));
        for(int i=0;i<y;i++){
        	HashMap<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.put(":yyyy", i);
            

            QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#yr = :yyyy").withNameMap(nameMap)
                .withValueMap(valueMap);

            ItemCollection<QueryOutcome> items = null;
            Iterator<Item> iterator = null;
            Item item = null;

            try {
                
                items = table.query(querySpec);

                iterator = items.iterator();
                while (iterator.hasNext()) {
                    item = iterator.next();
                    System.out.println(item.getNumber("userId") + "," + item.getNumber("movieId")+ "," + item.getNumber("rating"));
                }

            }
            catch (Exception e) {
                System.err.println("Unable to query");
                System.err.println(e.getMessage());
            }
        }
        

      
      
    }
}
