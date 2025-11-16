# Design Pattern Diagrams - Smart Seafood Chain

## 1. Factory Method Pattern

### Účel
Vytváření různých typů entit (Party, Food) bez specifikace konkrétní třídy.

```mermaid
classDiagram
    class PartyFactory {
        <<abstract>>
        +createParty(PartyType type, Config config)* Party
    }

    class FishermanFactory {
        +createParty(PartyType type, Config config) Fisherman
    }

    class ProcessorFactory {
        +createParty(PartyType type, Config config) Processor
    }

    class WarehouseFactory {
        +createParty(PartyType type, Config config) Warehouse
    }

    class Party {
        <<abstract>>
    }

    class Fisherman
    class Processor
    class Warehouse

    PartyFactory <|-- FishermanFactory
    PartyFactory <|-- ProcessorFactory
    PartyFactory <|-- WarehouseFactory
    
    FishermanFactory ..> Fisherman : creates
    ProcessorFactory ..> Processor : creates
    WarehouseFactory ..> Warehouse : creates
    
    Party <|-- Fisherman
    Party <|-- Processor
    Party <|-- Warehouse
```

### Použití
```java
PartyFactory factory = new FishermanFactory();
Party fisherman = factory.createParty(PartyType.FISHERMAN, config);
```

---

## 2. Builder Pattern

### Účel
Postupná konstrukce komplexních objektů (Transaction, Block).

```mermaid
classDiagram
    class TransactionBuilder {
        -Party from
        -Party to
        -Product product
        -OperationType type
        -Map~String,Object~ parameters
        +from(Party from) TransactionBuilder
        +to(Party to) TransactionBuilder
        +product(Product product) TransactionBuilder
        +type(OperationType type) TransactionBuilder
        +addParameter(String key, Object value) TransactionBuilder
        +build() Transaction
        -validate()
    }

    class Transaction {
        -String id
        -Party from
        -Party to
        -Product product
        -OperationType type
        -Map~String,Object~ parameters
        -long timestamp
        -String signature
    }

    class BlockBuilder {
        -int index
        -List~Transaction~ transactions
        -String previousHash
        -long timestamp
        +setIndex(int index) BlockBuilder
        +addTransaction(Transaction tx) BlockBuilder
        +setPreviousHash(String hash) BlockBuilder
        +setTimestamp(long ts) BlockBuilder
        +build() Block
        -calculateMerkleRoot() String
        -calculateHash() String
    }

    class Block {
        -int index
        -long timestamp
        -List~Transaction~ transactions
        -String previousHash
        -String merkleRoot
        -String hash
    }

    TransactionBuilder ..> Transaction : builds
    BlockBuilder ..> Block : builds
```

### Použití
```java
Transaction tx = new TransactionBuilder()
    .from(fisherman)
    .to(processor)
    .product(salmon)
    .type(OperationType.TRANSPORT)
    .addParameter("temperature", 4.0)
    .addParameter("duration", 120)
    .build();

Block block = new BlockBuilder()
    .setIndex(5)
    .setPreviousHash(previousBlock.getHash())
    .addTransaction(tx1)
    .addTransaction(tx2)
    .build();
```

---

## 3. State Pattern

### Účel
Řízení stavů ryby v potravinovém řetězci.

```mermaid
classDiagram
    class Food {
        -FoodState currentState
        +setState(FoodState state)
        +transport()
        +process()
        +store()
        +cook()
        +sell()
    }

    class FoodState {
        <<interface>>
        +transport(Food food)*
        +process(Food food)*
        +store(Food food)*
        +cook(Food food)*
        +sell(Food food)*
        +getName() String*
    }

    class CaughtState {
        +transport(Food food)
        +getName() String
    }

    class TransportedState {
        +process(Food food)
        +store(Food food)
        +getName() String
    }

    class ProcessedState {
        +cook(Food food)
        +store(Food food)
        +getName() String
    }

    class StoredState {
        +cook(Food food)
        +sell(Food food)
        +getName() String
    }

    class CookedState {
        +sell(Food food)
        +getName() String
    }

    class SoldState {
        +getName() String
    }

    Food o--> FoodState : currentState
    FoodState <|.. CaughtState
    FoodState <|.. TransportedState
    FoodState <|.. ProcessedState
    FoodState <|.. StoredState
    FoodState <|.. CookedState
    FoodState <|.. SoldState
    
    CaughtState ..> TransportedState : transitions to
    TransportedState ..> ProcessedState : transitions to
    TransportedState ..> StoredState : transitions to
    ProcessedState ..> StoredState : transitions to
    ProcessedState ..> CookedState : transitions to
    StoredState ..> CookedState : transitions to
    StoredState ..> SoldState : transitions to
    CookedState ..> SoldState : transitions to
```

### Sekvenční diagram přechodů
```mermaid
sequenceDiagram
    participant Client
    participant Food
    participant CaughtState
    participant TransportedState
    participant ProcessedState

    Client->>Food: transport()
    Food->>CaughtState: transport(food)
    CaughtState->>Food: setState(TransportedState)
    CaughtState-->>Client: OK

    Client->>Food: process()
    Food->>TransportedState: process(food)
    TransportedState->>Food: setState(ProcessedState)
    TransportedState-->>Client: OK
```

### Použití
```java
Fish salmon = new Fish("S001", FishType.SALMON, 5.0);
salmon.setState(new CaughtState());

salmon.transport(); // přechod do TransportedState
salmon.process();   // přechod do ProcessedState
salmon.cook();      // přechod do CookedState
salmon.sell();      // přechod do SoldState
```

---

## 4. Observer Pattern

### Účel
Event-driven komunikace mezi entitami.

```mermaid
classDiagram
    class EventManager {
        -Map~EventType,List~EventObserver~~ observers
        -EventQueue eventQueue
        +subscribe(EventType type, EventObserver observer)
        +unsubscribe(EventType type, EventObserver observer)
        +publish(Event event)
        +notifyObservers(Event event)
    }

    class EventObserver {
        <<interface>>
        +update(Event event)*
        +getObserverType() String*
    }

    class Event {
        <<abstract>>
        #String id
        #EventType type
        #long timestamp
        #Party source
        +getType() EventType
    }

    class AlertEvent {
        -Machine brokenMachine
        -int priority
        +getMachine() Machine
    }

    class DemandEvent {
        -Product product
        -int quantity
        -Money maxPrice
    }

    class Repairman {
        -boolean isAvailable
        +update(Event event)
        +handleAlert(AlertEvent alert)
    }

    class Distributor {
        +update(Event event)
        +handleDemand(DemandEvent demand)
    }

    EventManager "1" --> "*" EventObserver : notifies
    EventManager "1" --> "*" Event : manages
    Event <|-- AlertEvent
    Event <|-- DemandEvent
    EventObserver <|.. Repairman
    EventObserver <|.. Distributor
```

### Sekvenční diagram
```mermaid
sequenceDiagram
    participant Machine
    participant EventManager
    participant Repairman1
    participant Repairman2

    Machine->>Machine: wear exceeds threshold
    Machine->>EventManager: publish(AlertEvent)
    EventManager->>Repairman1: update(AlertEvent)
    EventManager->>Repairman2: update(AlertEvent)
    
    alt Repairman1 available
        Repairman1->>Machine: startRepair()
    else Repairman1 busy
        Repairman2->>Machine: startRepair()
    end
```

### Použití
```java
EventManager eventManager = new EventManager();

Repairman repairman1 = new Repairman("R1");
Repairman repairman2 = new Repairman("R2");

eventManager.subscribe(EventType.MACHINE_FAILURE, repairman1);
eventManager.subscribe(EventType.MACHINE_FAILURE, repairman2);

// Při poruše stroje
AlertEvent alert = new AlertEvent(brokenMachine, priority);
eventManager.publish(alert); // Notifikuje všechny Repairmany
```

---

## 5. Visitor Pattern

### Účel
Průchod hierarchií entit (SCMDirector, Inspector).

```mermaid
classDiagram
    class PartyVisitor {
        <<interface>>
        +visitFisherman(Fisherman f)*
        +visitProcessor(Processor p)*
        +visitWarehouse(Warehouse w)*
        +visitKitchen(Kitchen k)*
        +visitDistributor(Distributor d)*
        +visitSeller(Seller s)*
    }

    class Party {
        <<abstract>>
        +accept(PartyVisitor visitor)*
    }

    class Fisherman {
        +accept(PartyVisitor visitor)
    }

    class Processor {
        +accept(PartyVisitor visitor)
    }

    class Warehouse {
        +accept(PartyVisitor visitor)
    }

    class SCMDirector {
        -List~OptimizationAction~ actions
        +visitFisherman(Fisherman f)
        +visitProcessor(Processor p)
        +visitWarehouse(Warehouse w)
        +optimizeTransport()
        +adjustMargins()
    }

    class Inspector {
        -List~Inspection~ log
        +visitProcessor(Processor p)
        +visitWarehouse(Warehouse w)
        +visitKitchen(Kitchen k)
        +checkMachineWear()
        +checkQuality()
    }

    PartyVisitor <|.. SCMDirector
    PartyVisitor <|.. Inspector
    Party <|-- Fisherman
    Party <|-- Processor
    Party <|-- Warehouse
    Fisherman ..> PartyVisitor : accepts
    Processor ..> PartyVisitor : accepts
    Warehouse ..> PartyVisitor : accepts
```

### Sekvenční diagram
```mermaid
sequenceDiagram
    participant Client
    participant SCMDirector
    participant Fisherman
    participant Processor
    participant Warehouse

    Client->>Fisherman: accept(director)
    Fisherman->>SCMDirector: visitFisherman(this)
    SCMDirector->>SCMDirector: optimizeLogistics()
    
    Client->>Processor: accept(director)
    Processor->>SCMDirector: visitProcessor(this)
    SCMDirector->>SCMDirector: adjustMargins()
    
    Client->>Warehouse: accept(director)
    Warehouse->>SCMDirector: visitWarehouse(this)
    SCMDirector->>SCMDirector: checkInventory()
```

### Použití
```java
SCMDirector director = new SCMDirector();
Inspector inspector = new Inspector();

for (Party party : ecosystem.getAllParties()) {
    party.accept(director);   // Optimalizace
    party.accept(inspector);  // Kontrola kvality
}
```

---

## 6. Chain of Responsibility Pattern

### Účel
Zpracování oprav podle priority.

```mermaid
classDiagram
    class RepairHandler {
        <<abstract>>
        #RepairHandler next
        #int priorityThreshold
        +setNext(RepairHandler handler) RepairHandler
        +handle(RepairRequest request)*
        #canHandle(RepairRequest request) boolean*
        #processRequest(RepairRequest request)*
    }

    class HighPriorityHandler {
        +handle(RepairRequest request)
        #canHandle(RepairRequest request) boolean
        #processRequest(RepairRequest request)
    }

    class MediumPriorityHandler {
        +handle(RepairRequest request)
        #canHandle(RepairRequest request) boolean
        #processRequest(RepairRequest request)
    }

    class LowPriorityHandler {
        +handle(RepairRequest request)
        #canHandle(RepairRequest request) boolean
        #processRequest(RepairRequest request)
    }

    class RepairRequest {
        -Machine machine
        -int priority
        -long timestamp
        +getMachine() Machine
        +getPriority() int
    }

    class RepairmanPool {
        +getAvailableRepairman(int priority) Repairman
    }

    RepairHandler <|-- HighPriorityHandler
    RepairHandler <|-- MediumPriorityHandler
    RepairHandler <|-- LowPriorityHandler
    RepairHandler o--> RepairHandler : next
    RepairHandler ..> RepairRequest : handles
    RepairHandler ..> RepairmanPool : uses
```

### Sekvenční diagram
```mermaid
sequenceDiagram
    participant Client
    participant HighPriority
    participant MediumPriority
    participant LowPriority
    participant RepairmanPool

    Client->>HighPriority: handle(request priority=2)
    
    alt priority >= 3
        HighPriority->>RepairmanPool: getAvailableRepairman()
        RepairmanPool-->>HighPriority: repairman
        HighPriority->>Client: Handled
    else priority < 3
        HighPriority->>MediumPriority: handle(request)
        
        alt priority >= 2
            MediumPriority->>RepairmanPool: getAvailableRepairman()
            RepairmanPool-->>MediumPriority: repairman
            MediumPriority->>Client: Handled
        else priority < 2
            MediumPriority->>LowPriority: handle(request)
            LowPriority->>RepairmanPool: getAvailableRepairman()
            RepairmanPool-->>LowPriority: repairman
            LowPriority->>Client: Handled
        end
    end
```

### Použití
```java
RepairHandler chain = new HighPriorityHandler();
chain.setNext(new MediumPriorityHandler())
     .setNext(new LowPriorityHandler());

RepairRequest request = new RepairRequest(brokenMachine, priority);
chain.handle(request); // Zpracuje první vhodný handler
```

---

## 7. Memento Pattern

### Účel
Rekonstrukce stavu entity v minulosti.

```mermaid
classDiagram
    class Food {
        -String id
        -FoodState state
        -Map~String,Object~ data
        +createMemento(int tick) FoodMemento
        +restore(FoodMemento memento)
        +getCurrentState() FoodState
    }

    class FoodMemento {
        -String id
        -FoodState state
        -long timestamp
        -int tick
        -Map~String,Object~ data
        +getId() String
        +getState() FoodState
        +getTick() int
        -FoodMemento(Food food, int tick)
    }

    class Machine {
        -String id
        -double wear
        -boolean isBroken
        +createMemento(int tick) MachineMemento
        +restore(MachineMemento memento)
    }

    class MachineMemento {
        -String id
        -double wear
        -boolean isBroken
        -int tick
        -MachineMemento(Machine machine, int tick)
    }

    class MementoCaretaker {
        -Map~String,List~Memento~~ history
        +save(String entityId, Memento memento, int tick)
        +get(String entityId, int tick) Memento
        +getHistory(String entityId) List~Memento~
        +reconstructState(String entityId, int tick) Object
    }

    Food ..> FoodMemento : creates
    Machine ..> MachineMemento : creates
    MementoCaretaker "1" --> "*" FoodMemento : stores
    MementoCaretaker "1" --> "*" MachineMemento : stores
    FoodMemento --o Food : restores
    MachineMemento --o Machine : restores
```

### Sekvenční diagram
```mermaid
sequenceDiagram
    participant SimEngine
    participant Food
    participant Memento
    participant Caretaker

    loop každý takt
        SimEngine->>Food: createMemento(tick)
        Food->>Memento: new FoodMemento(this, tick)
        Memento-->>Food: memento
        Food->>Caretaker: save(id, memento, tick)
        Caretaker->>Caretaker: store in history
    end

    Note over Caretaker: Později - rekonstrukce stavu

    SimEngine->>Caretaker: get(foodId, tick=5)
    Caretaker-->>SimEngine: memento
    SimEngine->>Food: restore(memento)
    Food->>Food: setState(memento.state)
```

### Použití
```java
MementoCaretaker caretaker = new MementoCaretaker();

// Ukládání stavu každý takt
for (int tick = 0; tick < 10; tick++) {
    FoodMemento memento = fish.createMemento(tick);
    caretaker.save(fish.getId(), memento, tick);
}

// Rekonstrukce stavu v taktu 5
FoodMemento pastState = caretaker.get(fish.getId(), 5);
fish.restore(pastState);
System.out.println("State at tick 5: " + fish.getCurrentState());
```

---

## 8. Singleton Pattern

### Účel
Jediná instance Blockchain a SimulationEngine.

```mermaid
classDiagram
    class Blockchain {
        <<singleton>>
        -static Blockchain instance
        -Map~Channel,List~Block~~ chains
        -List~Transaction~ pendingTransactions
        -Blockchain()
        +static getInstance() Blockchain
        +addTransaction(Transaction tx, Channel channel)
        +createBlock(Channel channel) Block
        +validateChain(Channel channel) boolean
    }

    class SimulationEngine {
        <<singleton>>
        -static SimulationEngine instance
        -int currentTick
        -Ecosystem ecosystem
        -SimulationEngine()
        +static getInstance() Simulation Engine
        +runSimulation()
        +processTick()
    }

    note for Blockchain "Private constructor\nThread-safe lazy initialization"
    note for SimulationEngine "Ensures single point of control\nfor simulation"
```

### Použití
```java
// Získání instance
Blockchain blockchain = Blockchain.getInstance();
blockchain.addTransaction(transaction, channel);

SimulationEngine engine = SimulationEngine.getInstance();
engine.runSimulation();

// Nelze vytvořit novou instanci
// Blockchain b = new Blockchain(); // Compilation error!
```

---

## 9. Decorator Pattern

### Účel
Rozšiřování funkcionality strojů.

```mermaid
classDiagram
    class Machine {
        <<interface>>
        +process(Food food) Food*
        +getType() MachineType*
        +getPowerConsumption() double*
    }

    class BaseMachine {
        -String id
        -MachineType type
        -double powerConsumption
        +process(Food food) Food
        +getType() MachineType
        +getPowerConsumption() double
    }

    class MachineDecorator {
        <<abstract>>
        #Machine wrappedMachine
        +MachineDecorator(Machine machine)
        +process(Food food)* Food
        +getType() MachineType
        +getPowerConsumption() double
    }

    class AutomaticMachine {
        -double speedMultiplier
        +process(Food food) Food
        +getPowerConsumption() double
    }

    class MonitoredMachine {
        -MonitoringSystem monitor
        +process(Food food) Food
        +logOperation(Food food)
    }

    class EfficientMachine {
        -double efficiencyFactor
        +getPowerConsumption() double
    }

    Machine <|.. BaseMachine
    Machine <|.. MachineDecorator
    MachineDecorator <|-- AutomaticMachine
    MachineDecorator <|-- MonitoredMachine
    MachineDecorator <|-- EfficientMachine
    MachineDecorator o--> Machine : wraps
```

### Použití
```java
// Základní stroj
Machine basicMachine = new BaseMachine("M1", MachineType.FILLETING, 5.0);

// Obalení decoratory
Machine efficientMachine = new EfficientMachine(basicMachine, 0.8);
Machine monitoredMachine = new MonitoredMachine(efficientMachine);
Machine fullyEquipped = new AutomaticMachine(monitoredMachine, 1.5);

// Použití - všechny dekorátory se aplikují
Food processed = fullyEquipped.process(rawFood);
```

---

## 10. Object Pool Pattern

### Účel
Opětovné využití Repairman a Vehicle objektů.

```mermaid
classDiagram
    class ObjectPool~T~ {
        <<abstract>>
        #List~T~ available
        #List~T~ inUse
        #int maxSize
        +acquire() T
        +release(T object)
        +getAvailableCount() int
        +getInUseCount() int
        #createObject()* T
        #validateObject(T object) boolean
    }

    class RepairmanPool {
        -int maxRepairmen
        +acquire() Repairman
        +release(Repairman repairman)
        #createObject() Repairman
        #validateObject(Repairman r) boolean
    }

    class VehiclePool {
        -int maxVehicles
        +acquire() Vehicle
        +release(Vehicle vehicle)
        #createObject() Vehicle
        #validateObject(Vehicle v) boolean
    }

    class Repairman {
        -String id
        -boolean isAvailable
        +startRepair(Machine m)
        +finishRepair()
    }

    class Vehicle {
        -String id
        -boolean isAvailable
        -double fuelLevel
        +transport(Goods goods)
    }

    ObjectPool <|-- RepairmanPool
    ObjectPool <|-- VehiclePool
    RepairmanPool "1" --> "*" Repairman : manages
    VehiclePool "1" --> "*" Vehicle : manages
```

### Sekvenční diagram
```mermaid
sequenceDiagram
    participant Client
    participant Pool
    participant Repairman1
    participant Repairman2

    Client->>Pool: acquire()
    
    alt available list not empty
        Pool->>Pool: remove from available
        Pool->>Pool: add to inUse
        Pool-->>Client: Repairman1
    else available list empty
        alt pool not at maxSize
            Pool->>Pool: createObject()
            Pool->>Pool: add to inUse
            Pool-->>Client: new Repairman2
        else pool at maxSize
            Pool-->>Client: null (wait)
        end
    end

    Note over Client,Repairman1: Client uses Repairman1

    Client->>Pool: release(Repairman1)
    Pool->>Pool: validateObject(Repairman1)
    Pool->>Pool: remove from inUse
    Pool->>Pool: add to available
```

### Použití
```java
RepairmanPool pool = new RepairmanPool(5); // max 5 repairmen

// Získání z poolu
Repairman repairman = pool.acquire();
if (repairman != null) {
    repairman.startRepair(brokenMachine);
    // ... práce ...
    repairman.finishRepair();
    
    // Vrácení do poolu
    pool.release(repairman);
}

// Statistiky
System.out.println("Available: " + pool.getAvailableCount());
System.out.println("In use: " + pool.getInUseCount());
```

---

## 11. Stream API (Functional Programming)

### Použití v projektu

```java
// Filtrování transakcí podle typu
List<Transaction> salesTransactions = blockchain.getAllTransactions()
    .stream()
    .filter(tx -> tx.getType() == OperationType.SALE)
    .filter(tx -> tx.getTimestamp() > startTime)
    .collect(Collectors.toList());

// Agregace spotřeby energie
double totalPowerConsumption = productionLine.getMachines()
    .stream()
    .mapToDouble(Machine::getPowerConsumption)
    .sum();

// Hledání nejdražšího produktu
Optional<Product> mostExpensive = seller.getInventory()
    .keySet()
    .stream()
    .max(Comparator.comparing(seller::getPrice));

// Grouping transakcí podle kanálu
Map<Channel, List<Transaction>> transactionsByChannel = 
    transactions.stream()
        .collect(Collectors.groupingBy(Transaction::getChannel));

// Výpočet průměrné doby opravy
double avgRepairTime = repairLogs.stream()
    .mapToLong(log -> log.getEndTime() - log.getStartTime())
    .average()
    .orElse(0.0);

// Detekce double spending pomocí Stream API
boolean hasDoubleSpending = transactions.stream()
    .collect(Collectors.groupingBy(Transaction::getProductId, Collectors.counting()))
    .values()
    .stream()
    .anyMatch(count -> count > 1);

// Vytvoření reportu pomocí Stream API
String foodChainReport = foodHistory.stream()
    .sorted(Comparator.comparing(Transaction::getTimestamp))
    .map(tx -> String.format("%s: %s -> %s (%s)", 
        tx.getTimestamp(), 
        tx.getFrom().getName(), 
        tx.getTo().getName(),
        tx.getType()))
    .collect(Collectors.joining("\n"));
```

### Výhody použití Stream API:
1. **Deklarativní styl** - co chceme, ne jak to udělat
2. **Čitelnost** - zřetelný tok dat
3. **Kompozice operací** - řetězení operací
4. **Lazy evaluation** - operace se vykonávají až když je potřeba
5. **Paralelizace** - snadné použití `.parallel()`

