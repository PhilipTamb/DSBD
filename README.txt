ISTRUZIONI PER BUILD & DEPLOYMENT

INTELLIJ / SPRING + MAVEN
IntelliJ, più nello specifico Spring e Maven, potrebbe dare dei problemi in fase di build o semplicemente per creare i file jar dei microservizi. 
Gli errori sono causati da alcuni package e classi di moduli che non vengono importati correttamente (es. pratico: order-service non trova common-dto).
Se aggiungere nel pom.xml le dipendenze non risolve il problema, la soluzione è quella di aggiungere il file jar del modulo che Maven non riesce a trovare all'interno delle librerie del microservizio che genera l'errore.
Per fare questo, su IntelliJ bisogna andare su File --> Project Structure --> Modules (a sinistra) --> cliccare sul modulo che non riesce a buildare --> Tab Dependencies --> simbolo + --> Jars or Directories...
e selezionare il jar del modulo che il microservizio non riesce a trovare e che genera l'errore
Qui di seguito un breve elenco delle dipendenze:
il modulo ORDER-SERVICE ha bisogno di: COMMON-DTOS, MOVIE-SERVICE
il modulo PAYMENT-SERVICE ha bisogno di: COMMON-DTOS



DOCKER
se si necessita di provare il progetto solamente coi container docker, bisogna modificare le variabili d'ambiente di Kafka settate sul docker-compose (in particolare i listener e la porta);
bisogna inoltre modificare il bootstrap-server di payment-service e order-service che si trova nell'application.properties.
Prima di fare il comando "docker-compose" up bisogna buildare le immagini per ogni microservizio (N.B. commmon-dto non lo è) in quanto ogni modulo ha un proprio Dockerfile.
Il metodo più semplice è quello di recarsi in ogni sottocartella dei moduli (es: C:/progettoDSBD/saga-choreography-pattern/movie-service) ed eseguire il comando "docker build . -t movie-service"

KUBERNATES 
per deployare il progetto su kebernates bisogna pullare le immagini nel proprio docker-hub, questo può essere fatto attraverso i
comandi "docker login" per loggari al proprio docker-hub e poi è possibile eseguire "docker tag <Image ID> <cartella-docker-hub>/<docker-image-tag>
dopo questo può essere fatto "docker push  <cartella-docker-hub>/<docker-image-tag>".

Nel caso in cui si fa uso di questa soluzione si deve andare a specificare l'imagePullPolicy all'interno del 
file di kubernater .yml che nel nostro caso è "kubem.yml".
Questo andrà specificato nei "deployment" del file "kubem.yml" in particolare in:
spec: 
 containers: 
    image: index.docker.io/mrhiros/dsbd:payment-service
    imagePullPolicy: ""
In questo caso noi abbiamo messo nel nostro "kubem.yml" il campo "index.docker.io/mrhiros/dsbd:payment-service" poichè
"mrhiros/dsbd:payment-service" quessti sono il nostro nome utente/la nostra catella: il tag dell'immagine.
questo va ovviamente adattato in base alla propria cartella e al proprio nome utente.

In alternativa se si vuole recuperare le immagini da locale senza caricarle nel docker-hub si può lanciare minikube con il comando
"minikube start" e successivamente in base al sistema operativo che si usa si può lanciare:
PER WINDOWS: & minikube -p minikube docker-env | Invoke-Expression
PER LINUX: eval $(minikube -p minikube docker-env)

Nel caso in cui si faccio uso di quest'ultima soluzione si deve anche andare a specificare l'imagePullPolicy all'interno del 
file di kubernater .yml che nel nostro caso è "kubem.yml" in modo che kubernates andrà a recuperare la versione locale delle immagini.
Questo andrà specificato nei "deployment" del file "kubem.yml" in particolare in:
spec: 
 containers: 
    imagePullPolicy: never

a questo punto è possible fare il deployment di tutti il file "kubem.yml" in cui abbiamo pod, service e deployment.
Qusto viene fatto attrverso il comando "kubectl apply -f kubem.yml".


