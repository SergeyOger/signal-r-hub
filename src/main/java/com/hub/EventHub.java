package com.hub;

import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.signalr.SignalRGroupAction;
import com.microsoft.azure.functions.signalr.SignalRMessage;
import com.microsoft.azure.functions.signalr.annotation.SignalROutput;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

public class EventHub {

    public static void main(String[] args) {
        HubConnection hubConnection = HubConnectionBuilder.create("http://localhost:8080")
            .build();
        System.out.println(hubConnection.getConnectionState());
    }

    // Message for all clients
    //https://docs.microsoft.com/ru-ru/azure/azure-functions/functions-bindings-signalr-service-output?tabs=java
    @FunctionName("sendMessage")
    @SignalROutput(name = "$return", hubName = "chat")
    public SignalRMessage sendMessage(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Object> req) {

        SignalRMessage message = new SignalRMessage();
        message.target = "newMessage";
        message.arguments.add(req.getBody());
        return message;
    }

    // For selected user
    //https://docs.microsoft.com/ru-ru/azure/azure-functions/functions-bindings-signalr-service-output?tabs=java
    @FunctionName("sendMessage")
    @SignalROutput(name = "$return", hubName = "chat")
    public SignalRMessage sendMessageForTargetUser(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Object> req) {

        SignalRMessage message = new SignalRMessage();
        message.userId = "userId1";
        message.target = "newMessage";
        message.arguments.add(req.getBody());
        return message;
    }

    // Message for group
    //https://docs.microsoft.com/ru-ru/azure/azure-functions/functions-bindings-signalr-service-output?tabs=java
    @FunctionName("sendMessage")
    @SignalROutput(name = "$return", hubName = "chat")
    public SignalRMessage sendMessageToGroup(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Object> req) {

        SignalRMessage message = new SignalRMessage();
        message.groupName = "myGroup";
        message.target = "newMessage";
        message.arguments.add(req.getBody());
        return message;
    }

    // Adding user to group
    //https://docs.microsoft.com/ru-ru/azure/azure-functions/functions-bindings-signalr-service-output?tabs=java
    @FunctionName("addToGroup")
    @SignalROutput(name = "$return", hubName = "chat")
    public SignalRGroupAction addToGroup(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Object> req,
        @BindingName("userId") String userId) {

        SignalRGroupAction groupAction = new SignalRGroupAction();
        groupAction.action = "add";
        groupAction.userId = userId;
        groupAction.groupName = "myGroup";
        return groupAction;
    }

    // Delete user from group
    //https://docs.microsoft.com/ru-ru/azure/azure-functions/functions-bindings-signalr-service-output?tabs=java
    @FunctionName("removeFromGroup")
    @SignalROutput(name = "$return", hubName = "chat")
    public SignalRGroupAction removeFromGroup(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Object> req,
        @BindingName("userId") String userId) {

        SignalRGroupAction groupAction = new SignalRGroupAction();
        groupAction.action = "remove";
        groupAction.userId = userId;
        groupAction.groupName = "myGroup";
        return groupAction;
    }
}
