import React, { useState } from 'react';
import { FileText, GitBranch, Boxes, Settings } from 'lucide-react';

const SW2Documentation = () => {
  const [activeTab, setActiveTab] = useState('requirements');

  const tabs = [
    { id: 'requirements', label: 'Funkční požadavky', icon: FileText },
    { id: 'entities', label: 'Hlavní entity', icon: Boxes },
    { id: 'patterns', label: 'Design Patterns', icon: GitBranch },
    { id: 'architecture', label: 'Architektura', icon: Settings }
  ];

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-6xl mx-auto">
        <header className="bg-white rounded-lg shadow-sm p-6 mb-6">
          <h1 className="text-3xl font-bold text-gray-800 mb-2">
            🐟 Smart Seafood Chain - SW2 Návrh
          </h1>
          <p className="text-gray-600">
            Komplexní návrh systému pro sledování potravinového řetězce s blockchain technologií
          </p>
        </header>

        <div className="bg-white rounded-lg shadow-sm mb-6">
          <div className="flex border-b">
            {tabs.map(tab => {
              const Icon = tab.icon;
              return (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id)}
                  className={`flex items-center gap-2 px-6 py-3 font-medium transition-colors ${
                    activeTab === tab.id
                      ? 'border-b-2 border-blue-500 text-blue-600'
                      : 'text-gray-600 hover:text-gray-800'
                  }`}
                >
                  <Icon size={18} />
                  {tab.label}
                </button>
              );
            })}
          </div>

          <div className="p-6">
            {activeTab === 'requirements' && <RequirementsTab />}
            {activeTab === 'entities' && <EntitiesTab />}
            {activeTab === 'patterns' && <PatternsTab />}
            {activeTab === 'architecture' && <ArchitectureTab />}
          </div>
        </div>
      </div>
    </div>
  );
};

const RequirementsTab = () => (
  <div className="space-y-6">
    <section>
      <h2 className="text-2xl font-bold text-gray-800 mb-4">Systémové požadavky</h2>
      
      <div className="space-y-4">
        <RequirementCard
          id="FR-001"
          title="Blockchain transakce"
          priority="Povinný"
          description="Systém musí zaznamenávat každou operaci (lov, převoz, zpracování, skladování, vaření, balení, prodej) jako transakci v blockchainu. Transakce obsahuje identifikaci strany, parametry (teplota, doba, cena, kvalita), čas a abstraktní podpis."
        />
        
        <RequirementCard
          id="FR-002"
          title="Diskrétní simulace"
          priority="Povinný"
          description="Simulace probíhá v diskrétních taktech (1 takt = 1 hodina). V každém taktu probíhají operace sekvenčně v jednom vlákně. Systém postupně aplikuje změny na všech entitách."
        />
        
        <RequirementCard
          id="FR-003"
          title="Event-driven komunikace"
          priority="Povinný"
          description="Entity komunikují pomocí událostí (poptávka, porucha, dokončení operace). Události lze distribuovat více entitám, které jsou na ně zaregistrovány. Implementace pomocí Observer patternu."
        />
        
        <RequirementCard
          id="FR-004"
          title="Poruchy a opravy zařízení"
          priority="Povinný"
          description="Stroje a roboti se opotřebovávají a mohou se porouchat. Po poruše vzniká alert, který řeší opravář. Opraváři řeší poruchy podle priority a FIFO. Generují se události start/finish opravy."
        />
        
        <RequirementCard
          id="FR-005"
          title="Detekce double spending"
          priority="Povinný"
          description="Systém musí detekovat pokus o vícenásobný prodej stejného produktu (např. pod stejným certifikátem původu). Implementace kontroly v blockchain vrstvě."
        />
        
        <RequirementCard
          id="FR-006"
          title="Detekce manipulace blockchainu"
          priority="Povinný"
          description="Systém rozpozná pokus o zpětnou úpravu parametrů transakcí pomocí hash validace. Použití Merkle Tree pro ověření integrity."
        />
        
        <RequirementCard
          id="FR-007"
          title="Supply Chain Management"
          priority="Povinný"
          description="Ředitel SCM prochází ekosystémem a provádí optimalizace (přeprava, marže, dodací lhůty). Inspektor kontroluje opotřebení zařízení a kvalitu procesů. Visitor pattern pro průchod hierarchií."
        />
        
        <RequirementCard
          id="FR-008"
          title="Generování reportů"
          priority="Povinný"
          description="Systém generuje 5 typů reportů: FoodChainReport (cesta produktu), PartiesReport (přehled entit), FactoryConsumptionReport (spotřeba), SecurityReport (bezpečnostní incidenty), TransactionReport (finance a transakce), OutagesReport (statistiky výpadků)."
        />
        
        <RequirementCard
          id="FR-009"
          title="Blockchain kanály"
          priority="Povinný"
          description="Systém realizuje různé kanály pro typy produktů (čerstvé ryby, mražené produkty). Každá strana a transakce má definováno, v jakých kanálech může participovat."
        />
        
        <RequirementCard
          id="FR-010"
          title="Spotřeba a náklady"
          priority="Povinný"
          description="Stroje a roboti mají spotřebu (elektřina, materiál), lidé mají náklady na práci. Každá linka generuje report o spotřebě a finančním vyčíslení."
        />
        
        <RequirementCard
          id="FR-011"
          title="Dynamická poptávka"
          priority="Bonusový"
          description="Subjekty mohou posílat požadavky do kanálů. Ostatní účastníci reagují podle dostupnosti a ceny. Observer pattern pro registraci na typy požadavků."
        />
        
        <RequirementCard
          id="FR-012"
          title="Stavový automat ryby"
          priority="Bonusový"
          description="Ryba prochází stavy: ulovena → přepravena → zpracována → skladována → vařena/balena → prodána. State pattern pro řízení přechodů."
        />
        
        <RequirementCard
          id="FR-013"
          title="Rekonstrukce stavu"
          priority="Bonusový"
          description="Zrekonstruování stavu ryby nebo zařízení v libovolném taktu na základě historie blockchainu. Memento pattern pro ukládání stavů."
        />
      </div>
    </section>
  </div>
);

const RequirementCard = ({ id, title, priority, description }) => (
  <div className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
    <div className="flex items-start justify-between mb-2">
      <div className="flex items-center gap-2">
        <span className="text-sm font-mono text-gray-500">{id}</span>
        <h3 className="font-semibold text-gray-800">{title}</h3>
      </div>
      <span className={`text-xs px-2 py-1 rounded ${
        priority === 'Povinný' 
          ? 'bg-red-100 text-red-700' 
          : 'bg-blue-100 text-blue-700'
      }`}>
        {priority}
      </span>
    </div>
    <p className="text-sm text-gray-600">{description}</p>
  </div>
);

const EntitiesTab = () => (
  <div className="space-y-6">
    <h2 className="text-2xl font-bold text-gray-800 mb-4">Hlavní entity systému</h2>
    
    <div className="grid md:grid-cols-2 gap-4">
      <EntityCard
        title="Party (Strana)"
        subtitle="Abstraktní účastník řetězce"
        attributes={[
          'id: String',
          'name: String',
          'balance: Money',
          'channels: List<Channel>',
          'wallet: Wallet'
        ]}
        methods={[
          'sendTransaction(tx: Transaction)',
          'receiveTransaction(tx: Transaction)',
          'getBalance(): Money',
          'isInChannel(channel: Channel): boolean'
        ]}
      />
      
      <EntityCard
        title="Fisherman"
        subtitle="extends Party"
        attributes={[
          'catchCapacity: int',
          'fishingArea: String'
        ]}
        methods={[
          'catchFish(type: FishType, amount: int): Fish',
          'processTick(tick: int)'
        ]}
      />
      
      <EntityCard
        title="Processor"
        subtitle="extends Party"
        attributes={[
          'machines: List<Machine>',
          'processingLine: ProductionLine'
        ]}
        methods={[
          'processFood(food: Food): ProcessedFood',
          'handleMachineFailure(machine: Machine)'
        ]}
      />
      
      <EntityCard
        title="Warehouse"
        subtitle="extends Party"
        attributes={[
          'storage: Map<Food, Integer>',
          'temperature: double',
          'capacity: int'
        ]}
        methods={[
          'storeFood(food: Food, duration: int)',
          'retrieveFood(food: Food): Food',
          'checkQuality(food: Food): boolean'
        ]}
      />
      
      <EntityCard
        title="Kitchen"
        subtitle="extends Party"
        attributes={[
          'chefs: List<Chef>',
          'robots: List<RoboticChef>',
          'recipes: List<Recipe>'
        ]}
        methods={[
          'cookDish(recipe: Recipe, ingredients: List<Food>): Product',
          'assignChef(task: CookingTask): Chef'
        ]}
      />
      
      <EntityCard
        title="Distributor"
        subtitle="extends Party"
        attributes={[
          'vehicles: List<Vehicle>',
          'routes: List<Route>'
        ]}
        methods={[
          'transportGoods(from: Party, to: Party, goods: List<Food>)',
          'optimizeRoute(destinations: List<Party>)'
        ]}
      />
      
      <EntityCard
        title="Seller"
        subtitle="extends Party"
        attributes={[
          'inventory: Map<Product, Integer>',
          'priceList: Map<Product, Money>'
        ]}
        methods={[
          'sellProduct(product: Product, customer: Customer): Transaction',
          'updatePrice(product: Product, price: Money)'
        ]}
      />
      
      <EntityCard
        title="Customer"
        subtitle="extends Party"
        attributes={[
          'demands: List<Demand>'
        ]}
        methods={[
          'createDemand(product: Product, quantity: int): Demand',
          'buyProduct(product: Product, seller: Seller)'
        ]}
      />
      
      <EntityCard
        title="Blockchain"
        subtitle="Core component"
        attributes={[
          'chains: Map<Channel, List<Block>>',
          'pendingTransactions: List<Transaction>'
        ]}
        methods={[
          'addTransaction(tx: Transaction, channel: Channel)',
          'createBlock(channel: Channel): Block',
          'validateChain(channel: Channel): boolean',
          'detectDoubleSpending(product: Product): boolean',
          'detectTampering(): List<SecurityIncident>'
        ]}
      />
      
      <EntityCard
        title="Block"
        subtitle="Blockchain building block"
        attributes={[
          'index: int',
          'timestamp: long',
          'transactions: List<Transaction>',
          'previousHash: String',
          'merkleRoot: String',
          'hash: String'
        ]}
        methods={[
          'calculateHash(): String',
          'calculateMerkleRoot(): String',
          'validate(): boolean'
        ]}
      />
      
      <EntityCard
        title="Transaction"
        subtitle="Blockchain transaction"
        attributes={[
          'id: String',
          'from: Party',
          'to: Party',
          'product: Product',
          'operationType: OperationType',
          'parameters: Map<String, Object>',
          'timestamp: long',
          'signature: String'
        ]}
        methods={[
          'sign(party: Party)',
          'verify(): boolean',
          'toHash(): String'
        ]}
      />
      
      <EntityCard
        title="Machine"
        subtitle="Production equipment"
        attributes={[
          'id: String',
          'type: MachineType',
          'wear: double',
          'maxWear: double',
          'isBroken: boolean',
          'powerConsumption: double'
        ]}
        methods={[
          'process(food: Food): Food',
          'increaseWear(amount: double)',
          'breakDown(): AlertEvent',
          'repair()'
        ]}
      />
      
      <EntityCard
        title="Repairman"
        subtitle="Maintenance worker"
        attributes={[
          'id: String',
          'isAvailable: boolean',
          'currentTask: RepairTask'
        ]}
        methods={[
          'startRepair(machine: Machine, priority: int)',
          'finishRepair()',
          'handleAlert(event: AlertEvent)'
        ]}
      />
      
      <EntityCard
        title="SCMDirector"
        subtitle="Supply chain manager"
        attributes={[
          'optimization: OptimizationStrategy'
        ]}
        methods={[
          'visitParty(party: Party)',
          'optimizeTransport(from: Party, to: Party)',
          'adjustMargins(party: Party)',
          'generateReport(): SCMReport'
        ]}
      />
      
      <EntityCard
        title="Inspector"
        subtitle="Quality controller"
        attributes={[
          'inspectionLog: List<Inspection>'
        ]}
        methods={[
          'visitMachine(machine: Machine)',
          'checkQuality(process: Process): boolean',
          'logInspection(inspection: Inspection)'
        ]}
      />
      
      <EntityCard
        title="SimulationEngine"
        subtitle="Main controller"
        attributes={[
          'currentTick: int',
          'ecosystem: Ecosystem',
          'eventQueue: EventQueue',
          'blockchain: Blockchain'
        ]}
        methods={[
          'runTick()',
          'processEvents()',
          'updateEntities()',
          'generateReports()'
        ]}
      />
    </div>
  </div>
);

const EntityCard = ({ title, subtitle, attributes, methods }) => (
  <div className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
    <h3 className="font-bold text-gray-800 mb-1">{title}</h3>
    <p className="text-sm text-gray-500 mb-3">{subtitle}</p>
    
    <div className="mb-3">
      <h4 className="text-xs font-semibold text-gray-600 mb-1">Attributes:</h4>
      <ul className="text-xs text-gray-700 space-y-0.5 font-mono">
        {attributes.map((attr, i) => (
          <li key={i}>• {attr}</li>
        ))}
      </ul>
    </div>
    
    <div>
      <h4 className="text-xs font-semibold text-gray-600 mb-1">Methods:</h4>
      <ul className="text-xs text-gray-700 space-y-0.5 font-mono">
        {methods.map((method, i) => (
          <li key={i}>• {method}</li>
        ))}
      </ul>
    </div>
  </div>
);

const PatternsTab = () => (
  <div className="space-y-6">
    <h2 className="text-2xl font-bold text-gray-800 mb-4">Design Patterns</h2>
    
    <div className="space-y-4">
      <PatternCard
        name="1. Factory Method"
        purpose="Vytváření různých typů entit (Party, Food, Machine)"
        implementation="PartyFactory vytváří Fisherman, Processor, Warehouse atd. FoodFactory vytváří různé druhy ryb a produktů."
        classes={['PartyFactory', 'FoodFactory', 'Party (abstract)', 'Food (abstract)']}
        example="Party fisherman = PartyFactory.createFisherman('F1', 'John', area);"
      />
      
      <PatternCard
        name="2. Builder"
        purpose="Konstrukce komplexních objektů (Transaction, Block, Configuration)"
        implementation="TransactionBuilder pro postupné sestavení transakce s validací. BlockBuilder pro vytvoření bloku s všemi parametry. ConfigurationBuilder pro načtení konfigurace ekosystému."
        classes={['TransactionBuilder', 'BlockBuilder', 'EcosystemConfigBuilder']}
        example="Transaction tx = new TransactionBuilder().from(fisherman).to(processor).product(salmon).type(TRANSPORT).build();"
      />
      
      <PatternCard
        name="3. State"
        purpose="Řízení stavů ryby v potravinovém řetězci (bonusový požadavek)"
        implementation="Fish má aktuální stav (CaughtState, TransportedState, ProcessedState, StoredState, CookedState, SoldState). Každý stav definuje možné přechody a operace."
        classes={['FishState (interface)', 'CaughtState', 'TransportedState', 'ProcessedState', 'StoredState', 'CookedState', 'SoldState', 'Fish']}
        example="fish.setState(new TransportedState()); fish.process(); // přejde do ProcessedState"
      />
      
      <PatternCard
        name="4. Observer"
        purpose="Event-driven komunikace mezi entitami"
        implementation="EventManager spravuje observery. Entity se registrují na typy událostí (AlertEvent, DemandEvent, CompletionEvent). Při vzniku události jsou notifikovány všichni zaregistrovaní observeři."
        classes={['EventManager', 'Event (abstract)', 'EventObserver (interface)', 'AlertEvent', 'DemandEvent', 'RepairmanObserver']}
        example="eventManager.subscribe(EventType.MACHINE_FAILURE, repairman); eventManager.publish(new AlertEvent(machine));"
      />
      
      <PatternCard
        name="5. Visitor"
        purpose="Průchod hierarchií entit (SCMDirector, Inspector)"
        implementation="SCMDirector a Inspector implementují Visitor interface. Procházejí hierarchii Party objektů a provádějí specifické operace bez změny struktury navštívených tříd."
        classes={['PartyVisitor (interface)', 'SCMDirector', 'Inspector', 'Party (visitable)']}
        example="director.visit(fisherman); director.visit(processor); // optimalizace na každé Party"
      />
      
      <PatternCard
        name="6. Chain of Responsibility"
        purpose="Zpracování oprav podle priority"
        implementation="Repair requests procházejí řetězcem RepairHandlerů. Každý handler rozhoduje, zda požadavek zpracuje (podle priority a dostupnosti) nebo předá dalšímu v řetězci."
        classes={['RepairHandler (abstract)', 'HighPriorityHandler', 'MediumPriorityHandler', 'LowPriorityHandler']}
        example="highPriorityHandler.setNext(mediumPriorityHandler).setNext(lowPriorityHandler); highPriorityHandler.handle(repairRequest);"
      />
      
      <PatternCard
        name="7. Memento"
        purpose="Rekonstrukce stavu entity v minulosti (bonusový požadavek)"
        implementation="Entity vytváří memento objekty, které zachycují jejich stav. MementoCaretaker spravuje historii mement pro každý takt. Umožňuje rekonstrukci stavu v libovolném čase."
        classes={['EntityMemento', 'MementoCaretaker', 'Entity (originator)']}
        example="memento = fish.createMemento(tick); fish.restore(memento); // obnovení stavu z taktu"
      />
      
      <PatternCard
        name="8. Singleton"
        purpose="Jediná instance Blockchain a SimulationEngine"
        implementation="Blockchain a SimulationEngine jsou singleton objekty zajišťující globální přístup k centrálním komponentám systému. Thread-safe implementace."
        classes={['Blockchain', 'SimulationEngine']}
        example="Blockchain blockchain = Blockchain.getInstance(); blockchain.addTransaction(tx);"
      />
      
      <PatternCard
        name="9. Decorator"
        purpose="Rozšiřování funkcionality strojů"
        implementation="Základní Machine může být dekorován různými vlastnostmi (AutomaticMachine, MonitoredMachine, EfficientMachine) bez změny původní implementace."
        classes={['Machine (component)', 'MachineDecorator (decorator)', 'AutomaticMachine', 'MonitoredMachine']}
        example="Machine machine = new MonitoredMachine(new EfficientMachine(basicMachine));"
      />
      
      <PatternCard
        name="10. Object Pool"
        purpose="Opětovné využití Repairman a Vehicle objektů"
        implementation="RepairmanPool a VehiclePool spravují fondy objektů. Když není potřeba, objekt se vrací do poolu. Optimalizuje alokaci zdrojů."
        classes={['RepairmanPool', 'VehiclePool', 'ObjectPool<T>']}
        example="Repairman repairman = repairmanPool.acquire(); // práce; repairmanPool.release(repairman);"
      />
      
      <PatternCard
        name="11. Stream API"
        purpose="Funkcionální zpracování kolekcí"
        implementation="Použití Stream API pro filtrování transakcí, agregaci dat v reportech, hledání entit podle podmínek."
        classes={['Transaction', 'Report generators', 'Query operations']}
        example="List<Transaction> sales = transactions.stream().filter(t -> t.getType() == SALE).collect(Collectors.toList());"
      />
    </div>
  </div>
);

const PatternCard = ({ name, purpose, implementation, classes, example }) => (
  <div className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
    <h3 className="font-bold text-lg text-gray-800 mb-2">{name}</h3>
    
    <div className="space-y-2 text-sm">
      <div>
        <span className="font-semibold text-gray-700">Účel: </span>
        <span className="text-gray-600">{purpose}</span>
      </div>
      
      <div>
        <span className="font-semibold text-gray-700">Implementace: </span>
        <span className="text-gray-600">{implementation}</span>
      </div>
      
      <div>
        <span className="font-semibold text-gray-700">Klíčové třídy: </span>
        <div className="flex flex-wrap gap-1 mt-1">
          {classes.map((cls, i) => (
            <span key={i} className="bg-blue-50 text-blue-700 px-2 py-0.5 rounded text-xs font-mono">
              {cls}
            </span>
          ))}
        </div>
      </div>
      
      <div className="bg-gray-50 p-2 rounded mt-2">
        <span className="font-semibold text-gray-700 text-xs">Příklad použití:</span>
        <pre className="text-xs text-gray-700 mt-1 font-mono overflow-x-auto">{example}</pre>
      </div>
    </div>
  </div>
);

const ArchitectureTab = () => (
  <div className="space-y-6">
    <h2 className="text-2xl font-bold text-gray-800 mb-4">Architektura systému</h2>
    
    <section className="border border-gray-200 rounded-lg p-6">
      <h3 className="font-bold text-lg mb-4">Vrstvová architektura</h3>
      
      <div className="space-y-3">
        <ArchLayer
          name="1. Simulation Layer"
          description="Řídí běh simulace, zpracování taktů, koordinaci entit"
          components={['SimulationEngine', 'TimeManager', 'TickProcessor']}
        />
        
        <ArchLayer
          name="2. Business Logic Layer"
          description="Implementace byznyslové logiky, operace nad entitami"
          components={['Party hierarchie', 'Food hierarchie', 'Machine', 'ProductionLine', 'SCMDirector', 'Inspector']}
        />
        
        <ArchLayer
          name="3. Blockchain Layer"
          description="Správa blockchainu, transakcí, kanálů, validace integrity"
          components={['Blockchain', 'Block', 'Transaction', 'Channel', 'MerkleTree', 'SecurityValidator']}
        />
        
        <ArchLayer
          name="4. Event Layer"
          description="Event-driven komunikace mezi entitami"
          components={['EventManager', 'Event hierarchie', 'EventObserver', 'EventQueue']}
        />
        
        <ArchLayer
          name="5. Reporting Layer"
          description="Generování reportů, agregace dat, export do souborů"
          components={['ReportGenerator', 'FoodChainReport', 'SecurityReport', 'FactoryConsumptionReport', 'TransactionReport']}
        />
        
        <ArchLayer
          name="6. Configuration Layer"
          description="Načítání a správa konfigurace ekosystému"
          components={['ConfigurationLoader', 'YAMLParser', 'EcosystemBuilder']}
        />
      </div>
    </section>
    
    <section className="border border-gray-200 rounded-lg p-6">
      <h3 className="font-bold text-lg mb-4">Datové toky</h3>
      
      <div className="space-y-4">
        <DataFlow
          title="1. Transakční flow"
          steps={[
            'Party iniciuje operaci (např. prodej)',
            'Vytvoří se Transaction pomocí TransactionBuilder',
            'Transaction je validována a podepsána',
            'Přidána do pendingTransactions v Blockchain',
            'V dalším taktu je začleněna do nového Block',
            'Block je přidán do příslušného Channel',
            'Merkle root je vypočítán pro validaci integrity'
          ]}
        />
        
        <DataFlow
          title="2. Event flow"
          steps={[
            'Událost vzniká (např. porucha stroje)',
            'Machine vytvoří AlertEvent',
            'EventManager publikuje událost',
            'Všichni zaregistrovaní observeři jsou notifikováni',
            'Repairman přijímá událost a zpracovává ji',
            'Po zpracování generuje CompletionEvent'
          ]}
        />
        
        <DataFlow
          title="3. Simulační flow"
          steps={[
            'SimulationEngine spustí nový takt',
            'Zpracují se všechny události z EventQueue',
            'Entity provádějí své akce (lov, zpracování, prodej)',
            'Aktualizuje se stav entit (opotřebení, zásoby)',
            'Transakce jsou zapsány do blockchainu',
            'Generují se reporty pro aktuální takt',
            'Přechod na další takt'
          ]}
        />
      </div>
    </section>
    
    <section className="border border-gray-200 rounded-lg p-6">
      <h3 className="font-bold text-lg mb-4">Klíčové balíčky</h3>
      
      <div className="grid md:grid-cols-2 gap-3">
        <PackageCard name="cz.cvut.fel.omo.seafood.simulation" classes={['SimulationEngine', 'TimeManager', 'TickProcessor']} />
        <PackageCard name="cz.cvut.fel.omo.seafood.party" classes={['Party', 'Fisherman', 'Processor', 'Warehouse', 'Kitchen']} />
        <PackageCard name="cz.cvut.fel.omo.seafood.blockchain" classes={['Blockchain', 'Block', 'Transaction', 'Channel']} />
        <PackageCard name="cz.cvut.fel.omo.seafood.food" classes={['Food', 'Fish', 'ProcessedFood', 'Product']} />
        <PackageCard name="cz.cvut.fel.omo.seafood.machine" classes={['Machine', 'Robot', 'ProductionLine']} />
        <PackageCard name="cz.cvut.fel.omo.seafood.event" classes={['Event', 'EventManager', 'EventObserver']} />
        <PackageCard name="cz.cvut.fel.omo.seafood.report" classes={['ReportGenerator', 'FoodChainReport', 'SecurityReport']} />
        <PackageCard name="cz.cvut.fel.omo.seafood.visitor" classes={['PartyVisitor', 'SCMDirector', 'Inspector']} />
        <PackageCard name="cz.cvut.fel.omo.seafood.state" classes={['FishState', 'CaughtState', 'ProcessedState']} />
        <PackageCard name="cz.cvut.fel.omo.seafood.config" classes={['ConfigurationLoader', 'YAMLParser', 'EcosystemBuilder']} />
      </div>
    </section>
    
    <section className="border border-gray-200 rounded-lg p-6">
      <h3 className="font-bold text-lg mb-4">Ukázková konfigurace</h3>
      
      <div className="bg-gray-50 p-4 rounded">
        <pre className="text-xs font-mono text-gray-700 overflow-x-auto whitespace-pre-wrap">
{`ecosystem:
  parties:
    - type: Fisherman
      id: F1
      name: "Captain John"
      balance: 10000
      catchCapacity: 100
      fishingArea: "North Sea"
      channels: [fresh_fish]
    
    - type: Fisherman
      id: F2
      name: "Captain Maria"
      balance: 12000
      catchCapacity: 150
      fishingArea: "Atlantic"
      channels: [fresh_fish]
    
    - type: Processor
      id: P1
      name: "SeaFood Processing Co."
      balance: 50000
      channels: [fresh_fish, processed_fish]
      machines:
        - type: Filleting
          powerConsumption: 5.0
          maxWear: 100.0
        - type: Cleaning
          powerConsumption: 3.0
          maxWear: 100.0
    
    - type: Warehouse
      id: W1
      name: "Cold Storage Inc."
      balance: 30000
      capacity: 1000
      temperature: 4.0
      channels: [fresh_fish, processed_fish]
    
    - type: Kitchen
      id: K1
      name: "Sushi Master Kitchen"
      balance: 40000
      channels: [processed_fish, cooked_products]
      chefs: 3
      robots: 2
    
    - type: Distributor
      id: D1
      name: "Fast Delivery Ltd."
      balance: 25000
      channels: [fresh_fish, processed_fish, cooked_products]
      vehicles: 5
    
    - type: Seller
      id: S1
      name: "Fresh Market"
      balance: 35000
      channels: [cooked_products, fresh_fish]
    
    - type: Seller
      id: S2
      name: "Sushi Restaurant"
      balance: 45000
      channels: [cooked_products]

  foodTypes:
    - name: Salmon
      category: fresh_fish
      basePrice: 15.0
    - name: Tuna
      category: fresh_fish
      basePrice: 20.0
    - name: Shrimp
      category: fresh_fish
      basePrice: 12.0
    - name: Cod
      category: fresh_fish
      basePrice: 10.0
    - name: Sardine
      category: fresh_fish
      basePrice: 5.0

  productionLines:
    - name: "Fresh Products Line"
      channel: processed_fish
      machines: [Filleting, Cleaning, Packaging]
    
    - name: "Frozen Products Line"
      channel: processed_fish
      machines: [Filleting, Freezing, Packaging]

  roles:
    - type: SCMDirector
      id: SCM1
      name: "Supply Chain Manager"
    
    - type: Inspector
      id: INS1
      name: "Quality Inspector"
    
    - type: Repairman
      id: R1
      name: "Technician John"
    
    - type: Repairman
      id: R2
      name: "Technician Sarah"

  simulation:
    totalTicks: 50
    tickDuration: 3600  # 1 hour in seconds
    startBalance: 10000`}
        </pre>
      </div>
    </section>
  </div>
);

const ArchLayer = ({ name, description, components }) => (
  <div className="bg-blue-50 border-l-4 border-blue-500 p-4 rounded">
    <h4 className="font-semibold text-gray-800 mb-1">{name}</h4>
    <p className="text-sm text-gray-600 mb-2">{description}</p>
    <div className="flex flex-wrap gap-1">
      {components.map((comp, i) => (
        <span key={i} className="bg-white px-2 py-1 rounded text-xs text-gray-700 border border-blue-200">
          {comp}
        </span>
      ))}
    </div>
  </div>
);

const DataFlow = ({ title, steps }) => (
  <div className="border-l-4 border-green-500 pl-4">
    <h4 className="font-semibold text-gray-800 mb-2">{title}</h4>
    <ol className="space-y-1">
      {steps.map((step, i) => (
        <li key={i} className="text-sm text-gray-600">
          <span className="font-semibold text-green-600">{i + 1}.</span> {step}
        </li>
      ))}
    </ol>
  </div>
);

const PackageCard = ({ name, classes }) => (
  <div className="bg-gray-50 p-3 rounded border border-gray-200">
    <h4 className="font-mono text-xs text-blue-600 mb-2">{name}</h4>
    <ul className="space-y-0.5">
      {classes.map((cls, i) => (
        <li key={i} className="text-xs text-gray-700">• {cls}</li>
      ))}
    </ul>
  </div>
);

export default SW2Documentation;