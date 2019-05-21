# Architecture choices

## EventVisitor
In order to crawle the bytecode of a project, we have decided to use a single one visitor that is called EventVisitor. This visitor is in charge of dispatching the rights events during the visits of the bytecoce. We have take this solution in order to have a simple Rule API and also the best performance we could have.

## The use of events
The whole project lie on the idea of events and events listeners. In fact, in order to reduce the API of a rule we have decided that each visit method, during an asm visit, will dispatch an event of specific type with the argument of the visit method. In order to "consume" those events we have also introduce the concept of event listener. So each rules have the responsability to register a lambda to a specific event type. Thereby, when our EventVisitor call a visit method, it create a new event of the type of the method and then call every consumer of this events. Thanks to that, rules can easily subscribe to event and provide a check for specific condition. If the rule want to add a statement on something going wrong on the code that it is being analyse, then, it can dispatch an event of type AddRecord. Thanks to that, our Runner can easily handle the logic of adding a record inside the xml report.

At the moment, a rule register to specific event thanks to the register method inside the interface of a rule. This is what we call an inversion of control.
Then, a rule can do this in the register method :
```java
@Override
public void register(EventSubscriber subscriber) {
    subscriber.subscribe(VisitFieldEvent.class, (event, dispatcher) -> {
       //logic
       if (somethingIsTrue) {
           dispatcher.dispatch(new AddRecordEvent(...));
       }
    });
}
```

## CallbackVisitor
Because sometimes your Rule need to do additional analyses not especially in the scope of the project then we have create the CallbackVisitor class. This object allows you to specify some callbacks/consumer on a additional class. Let's take an example to explain it better. So we take the case of a Rule that need to make a check on the parent object of the current class. We could have a code like this :

```java
@Override
public void register(EventSubscriber subscriber) {
    subscriber.subscribe(VisitEvent.class, (event, dispatcher) -> {
        CallbackVisitor visitor = new CallbackVisitor.Builder()
        .onVisit(e -> {
            //logic on the event of the additional analysis
            if (somethingIsTrue) {
                dispatcher.dispatch(new AddRecordEvent(...));
            }
        })
        .toVisitor();
    });
}
```

With the following code you only create a nodeVisitor that will call your callback when the check will be done. Now you need to say to our Runner that you want to do an additional analysis. Let's take our former example again :

```java
@Override
public void register(EventSubscriber subscriber) {
    subscriber.subscribe(VisitEvent.class, (event, dispatcher) -> {
        CallbackVisitor visitor = new CallbackVisitor.Builder()
        .onVisit(e -> {
            //logic on the event of the additional analysis
            if (somethingIsTrue) {
                dispatcher.dispatch(new AddRecordEvent(...));
            }
        })
        .toVisitor();

        String addClass = event.getSuperName();
        dispatcher.dispatch(new AddAdditionalEvent(addClass, visitor));
    });
}
```

Now at the end of the project's check then all additional analysis will be done. This allow us to gain performance because if multiple Rule wants to analysis same new class (witch is morelikely to append) then we can regroup all the callbacks together and only run one bytecode crawle.

## The simplification of the XML
We chose to simplify the achitecture of the XML file in the latest version of our project for several reasons. We realized that the previous version of the XML file was complicated to maintain and required a sorting operation to make a write possible given the hierarchy. Example of the old hierarchy :

```xml
<classes>
     <class name="abc.class">
          <rules>
               <rule name="instanceof">
                    <record>....</record>
                    <record>....</record>
                    <record>....</record>
               </rule>
               <rule name="anonymous">
                    <record>....</record>
                    <record>....</record>
                    <record>....</record>
               </rule>
          </rules>
     </class>
</classes>
```
The new version follows a much simpler format that must allow to meet foraxproof constraints. In particular, this new format allows us to make unique entries for each detection by providing all necessary information on the <record> line in question.
Thus, it is possible to make writes at any time, it will be easier to set up the concurency with this model but also it allows us to delete a large part of the lists in memory and the expensive sorting operation that we were doing in the program.
Sorting by class could be done in the future directly in the JS script, using the resources of the client machine.
The new structure is :
```xml
<records>
     <record context="ExampleClass.class" rule="Abstract" level="WARNING">ExampleClass class shouldn't be public</record>
     <record context="ExampleClass.class" rule="Protected" level="WARNING">protectedString field shouldn't use protected visibility</record>
</records>
```

## Registrable

Every Registrable (Rule) should be thread safe.

## Memory model

| Class | Should be thread safe | Number in thread | Number in program |
|---	|---                    |---               |--- |
| EventVisitor | N | 1 | n |
| CallbackVisitor | N | 1 | n |
| fr.upem.foraxproof.core.handler.* | Y | 1 | 1 |
| EventManager | Y | 1 | 1 |
| CallbackManager | N | 1 | n |
| ASM Events (can be called multiple times) | Y | n | n |
| APP Events (can be called multiple times) | Y | n | n |
| StartEvent | N | 1 | 1 |
| EndEvent | N | 1 | 1 |

# Difficulties encountered

## Non mutable AnonymousRule
We have some difficulties to create an unmutable AnonymousRule because some treatments needs to be performed on Visit and others on VisitMethods.
@Fixed

## AnonymousRule with parameter type <T> or <? super ...>
We have tried to make AnonymousRule recognized parameter type in the following MR :
https://gitlab.com/esipeinfo2017/PLIEZ-RASQUIER/APVRProject/merge_requests/59
and also :
https://gitlab.com/esipeinfo2017/PLIEZ-RASQUIER/APVRProject/merge_requests/60

But we have decide to not merge it because of several issues.
