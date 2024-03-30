/*
Name: Paul Marchitiello
Section Number: 01
System Specs:
processor       : 0
vendor_id       : GenuineIntel
cpu family      : 6
model           : 58
model name      : Intel(R) Xeon(R) Gold 6254 CPU @ 3.10GHz
stepping        : 0
microcode       : 0x5003604
cpu MHz         : 3092.734
cache size      : 25344 KB
physical id     : 0
siblings        : 2
core id         : 0
cpu cores       : 2
apicid          : 0
initial apicid  : 0
fpu             : yes
fpu_exception   : yes
cpuid level     : 13
wp              : yes
flags           : fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush mmx fxsr sse sse2 ss ht syscall nx rdtscp lm constant_tsc arch_perfmon nopl xtopology tsc_reliable nonstop_tsc cpuid tsc_known_freq pni pclmulqdq ssse3 cx16 pcid sse4_1 sse4_2 x2apic popcnt tsc_deadline_timer aes xsave avx f16c rdrand hypervisor lahf_lm pti ssbd ibrs ibpb stibp fsgsbase tsc_adjust smep arat md_clear flush_l1d arch_capabilities
bugs            : cpu_meltdown spectre_v1 spectre_v2 spec_store_bypass l1tf mds swapgs itlb_multihit srbds mmio_unknown
bogomips        : 6185.46
clflush size    : 64
cache_alignment : 64
address sizes   : 43 bits physical, 48 bits virtual
power management:

processor       : 1
vendor_id       : GenuineIntel
cpu family      : 6
model           : 58
model name      : Intel(R) Xeon(R) Gold 6254 CPU @ 3.10GHz
stepping        : 0
microcode       : 0x5003604
cpu MHz         : 3092.734
cache size      : 25344 KB
physical id     : 0
siblings        : 2
core id         : 1
cpu cores       : 2
apicid          : 1
initial apicid  : 1
fpu             : yes
fpu_exception   : yes
cpuid level     : 13
wp              : yes
flags           : fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush mmx fxsr sse sse2 ss ht syscall nx rdtscp lm constant_tsc arch_perfmon nopl xtopology tsc_reliable nonstop_tsc cpuid tsc_known_freq pni pclmulqdq ssse3 cx16 pcid sse4_1 sse4_2 x2apic popcnt tsc_deadline_timer aes xsave avx f16c rdrand hypervisor lahf_lm pti ssbd ibrs ibpb stibp fsgsbase tsc_adjust smep arat md_clear flush_l1d arch_capabilities
bugs            : cpu_meltdown spectre_v1 spectre_v2 spec_store_bypass l1tf mds swapgs itlb_multihit srbds mmio_unknown
bogomips        : 6185.46
clflush size    : 64
cache_alignment : 64
address sizes   : 43 bits physical, 48 bits virtual
power management:

processor       : 2
vendor_id       : GenuineIntel
cpu family      : 6
model           : 58
model name      : Intel(R) Xeon(R) Gold 6254 CPU @ 3.10GHz
stepping        : 0
microcode       : 0x5003604
cpu MHz         : 3092.734
cache size      : 25344 KB
physical id     : 1
siblings        : 2
core id         : 0
cpu cores       : 2
apicid          : 2
initial apicid  : 2
fpu             : yes
fpu_exception   : yes
cpuid level     : 13
wp              : yes
flags           : fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush mmx fxsr sse sse2 ss ht syscall nx rdtscp lm constant_tsc arch_perfmon nopl xtopology tsc_reliable nonstop_tsc cpuid tsc_known_freq pni pclmulqdq ssse3 cx16 pcid sse4_1 sse4_2 x2apic popcnt tsc_deadline_timer aes xsave avx f16c rdrand hypervisor lahf_lm pti ssbd ibrs ibpb stibp fsgsbase tsc_adjust smep arat md_clear flush_l1d arch_capabilities
bugs            : cpu_meltdown spectre_v1 spectre_v2 spec_store_bypass l1tf mds swapgs itlb_multihit srbds mmio_unknown
bogomips        : 6185.46
clflush size    : 64
cache_alignment : 64
address sizes   : 43 bits physical, 48 bits virtual
power management:

processor       : 3
vendor_id       : GenuineIntel
cpu family      : 6
model           : 58
model name      : Intel(R) Xeon(R) Gold 6254 CPU @ 3.10GHz
stepping        : 0
microcode       : 0x5003604
cpu MHz         : 3092.734
cache size      : 25344 KB
physical id     : 1
siblings        : 2
core id         : 1
cpu cores       : 2
apicid          : 3
initial apicid  : 3
fpu             : yes
fpu_exception   : yes
cpuid level     : 13
wp              : yes
flags           : fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush mmx fxsr sse sse2 ss ht syscall nx rdtscp lm constant_tsc arch_perfmon nopl xtopology tsc_reliable nonstop_tsc cpuid tsc_known_freq pni pclmulqdq ssse3 cx16 pcid sse4_1 sse4_2 x2apic popcnt tsc_deadline_timer aes xsave avx f16c rdrand hypervisor lahf_lm pti ssbd ibrs ibpb stibp fsgsbase tsc_adjust smep arat md_clear flush_l1d arch_capabilities
bugs            : cpu_meltdown spectre_v1 spectre_v2 spec_store_bypass l1tf mds swapgs itlb_multihit srbds mmio_unknown
bogomips        : 6185.46
clflush size    : 64
cache_alignment : 64
address sizes   : 43 bits physical, 48 bits virtual
power management:*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/timeb.h>
#include <semaphore.h>

#define MAX_SIZE 100000000
#define MAX_THREADS 16
#define RANDOM_SEED 7649
#define MAX_RANDOM_NUMBER 3000
#define NUM_LIMIT 9973

// Global variables
long gRefTime; //For timing
int gData[MAX_SIZE]; //The array that will hold the data

int gThreadCount; //Number of threads
int gDoneThreadCount; //Number of threads that are done at a certain point. Whenever a thread is done, it increments this. Used with the semaphore-based solution
int gThreadProd[MAX_THREADS]; //The modular product for each array division that a single thread is responsible for
bool gThreadDone[MAX_THREADS]; //Is this thread done? Used when the parent is continually checking on child threads

// Semaphores
sem_t completed; //To notify parent that all threads have completed or one of them found a zero
sem_t mutex; //Binary semaphore to protect the shared variable gDoneThreadCount

int SqFindProd(int size); //Sequential FindProduct (no threads) computes the product of all the elements in the array mod NUM_LIMIT
void *ThFindProd(void *param); //Thread FindProduct but without semaphores
void *ThFindProdWithSemaphore(void *param); //Thread FindProduct with semaphores
int ComputeTotalProduct(); // Multiply the division products to compute the total modular product 
void InitSharedVars();
void GenerateInput(int size, int indexForZero); //Generate the input array
void CalculateIndices(int arraySize, int thrdCnt, int indices[MAX_THREADS][3]); //Calculate the indices to divide the array into T divisions, one division per thread
int GetRand(int min, int max);//Get a random number between min and max

//Timing functions
long GetMilliSecondTime(struct timeb timeBuf);
long GetCurrentTime(void);
void SetTime(void);
long GetTime(void);

int main(int argc, char *argv[]){

	pthread_t tid[MAX_THREADS];
	pthread_attr_t attr[MAX_THREADS];
	int indices[MAX_THREADS][3];
	int i, indexForZero, arraySize, prod;

	// Code for parsing and checking command-line arguments
	if(argc != 4){
		fprintf(stderr, "Invalid number of arguments!\n");
		exit(-1);
	}
	if((arraySize = atoi(argv[1])) <= 0 || arraySize > MAX_SIZE){
		fprintf(stderr, "Invalid Array Size\n");
		exit(-1);
	}
	gThreadCount = atoi(argv[2]);
	if(gThreadCount > MAX_THREADS || gThreadCount <=0){
		fprintf(stderr, "Invalid Thread Count\n");
		exit(-1);
	}
	indexForZero = atoi(argv[3]);
	if(indexForZero < -1 || indexForZero >= arraySize){
		fprintf(stderr, "Invalid index for zero!\n");
		exit(-1);
	}

    GenerateInput(arraySize, indexForZero);

    CalculateIndices(arraySize, gThreadCount, indices);

	// Code for the sequential part
	SetTime();
	prod = SqFindProd(arraySize);
	printf("Sequential multiplication completed in %ld ms. Product = %d\n", GetTime(), prod);
/*
	// Threaded with parent waiting for all child threads
	InitSharedVars();
	SetTime();

	// Write your code here
	// Initialize threads, create threads, and then let the parent wait for all threads using pthread_join
	// The thread start function is ThFindProd
	// Don't forget to properly initialize shared variables
	for (int i = 0; i < gThreadCount; i++){
		pthread_attr_init(&attr[i]);
		pthread_create(&tid[i], &attr[i], ThFindProd, indices[i]);
	}

	for(int i = 0; i < gThreadCount; i++){
		pthread_join(tid[i], NULL);
	}

    prod = ComputeTotalProduct();
	printf("Threaded multiplication with parent waiting for all children completed in %ld ms. Product = %d\n", GetTime(), prod);
*/
	// Multi-threaded with busy waiting (parent continually checking on child threads without using semaphores)
	InitSharedVars();
	SetTime();

	// Write your code here
    // Don't use any semaphores in this part
	// Initialize threads, create threads, and then make the parent continually check on all child threads
	// The thread start function is ThFindProd
	// Don't forget to properly initialize shared variables
	for (int i = 0; i < gThreadCount; i++){
		pthread_attr_init(&attr[i]);
		pthread_create(&tid[i], &attr[i], ThFindProd, indices[i]);
	}

/*
	bool allChildrenDone = false;
	while (allChildrenDone == false)
	{
		allChildrenDone = true;
		for(int i = 0; i < gThreadCount; i++){
			if (gThreadDone[i] == false){
				allChildrenDone = false;
			}	
		}
	}

	for (int i = 0; i < gThreadCount; i++){
		pthread_cancel(tid[i]);
	}
*/
    prod = ComputeTotalProduct();
	printf("Threaded multiplication with parent continually checking on children completed in %ld ms. Product = %d\n", GetTime(), prod);


	// Multi-threaded with semaphores

	InitSharedVars();
    // Initialize your semaphores here
	sem_init(&mutex,0,1);

	SetTime();

    // Write your code here
	// Initialize threads, create threads, and then make the parent wait on the "completed" semaphore
	// The thread start function is ThFindProdWithSemaphore
	// Don't forget to properly initialize shared variables and semaphores using sem_init
	for (int i = 0; i < gThreadCount; i++){
		pthread_attr_init(&attr[i]);
		pthread_create(&tid[i], &attr[i], ThFindProdWithSemaphore, indices[i]);
	}

	sem_wait(&completed);
	
	for (int i = 0; i < gThreadCount; i++){
		pthread_cancel(tid[i]);
	}

    prod = ComputeTotalProduct();
	printf("Threaded multiplication with parent waiting on a semaphore completed in %ld ms. Prod = %d\n", GetTime(), prod);
	
}

// Write a regular sequential function to multiply all the elements in gData mod NUM_LIMIT
// REMEMBER TO MOD BY NUM_LIMIT AFTER EACH MULTIPLICATION TO PREVENT YOUR PRODUCT VARIABLE FROM OVERFLOWING
int SqFindProd(int size) {
	int productResult = gData[0];
	for (int i = 1; i < size; i++){
		if(gData[i] == 0)
			break;

		productResult *= gData[i];
		productResult %= NUM_LIMIT;
	}
	return productResult;
}

// Write a thread function that computes the product of all the elements in one division of the array mod NUM_LIMIT
// REMEMBER TO MOD BY NUM_LIMIT AFTER EACH MULTIPLICATION TO PREVENT YOUR PRODUCT VARIABLE FROM OVERFLOWING
// When it is done, this function should store the product in gThreadProd[threadNum] and set gThreadDone[threadNum] to true
void* ThFindProd(void *param) {
	int threadNum = ((int*)param)[0];
	int startIndex = ((int*)param)[1];
	int endIndex = ((int*)param)[2];

	for (int i = startIndex; i < endIndex; i++){
		pthread_testcancel();
		if(gData[i] == 0){
			gThreadDone[threadNum] = true;
			break;
		}	
		else{
			gThreadProd[threadNum] *= gData[i];
			gThreadProd[threadNum] %= NUM_LIMIT;
			printf("%d: %d \n", i, gThreadProd[threadNum]);
		}
	}

	gThreadDone[threadNum] = true;
	pthread_exit(0);
}

// Write a thread function that computes the product of all the elements in one division of the array mod NUM_LIMIT
// REMEMBER TO MOD BY NUM_LIMIT AFTER EACH MULTIPLICATION TO PREVENT YOUR PRODUCT VARIABLE FROM OVERFLOWING
// When it is done, this function should store the product in gThreadProd[threadNum]
// If the product value in this division is zero, this function should post the "completed" semaphore
// If the product value in this division is not zero, this function should increment gDoneThreadCount and
// post the "completed" semaphore if it is the last thread to be done
// Don't forget to protect access to gDoneThreadCount with the "mutex" semaphore
void* ThFindProdWithSemaphore(void *param) {
	int threadNum = ((int*)param)[0];
	int startIndex = ((int*)param)[1];
	int endIndex = ((int*)param)[2];

	for (int i = startIndex; i < endIndex; i++){
		pthread_testcancel();
		if(gData[i] == 0){
			sem_post(&completed);
		}	

		gThreadProd[threadNum] *= gData[i];
		gThreadProd[threadNum] %= NUM_LIMIT;
	}

	sem_wait(&mutex);
	gDoneThreadCount++;
	if(gDoneThreadCount == gThreadCount)
		sem_post(&completed);
	sem_post(&mutex);
	pthread_exit(0);
}

int ComputeTotalProduct() {
    int i, prod = 1;

	for(i=0; i<gThreadCount; i++)
	{
		prod *= gThreadProd[i];
		prod %= NUM_LIMIT;
	}

	return prod;
}

void InitSharedVars() {
	int i;

	for(i=0; i<gThreadCount; i++){
		gThreadDone[i] = false;
		gThreadProd[i] = 1;
	}
	gDoneThreadCount = 0;
}

// Write a function that fills the gData array with random numbers between 1 and MAX_RANDOM_NUMBER
// If indexForZero is valid and non-negative, set the value at that index to zero
void GenerateInput(int size, int indexForZero) {

	for(int i = 0; i < size; i++){
		if (i == indexForZero)
			gData[i] = 0;
		else
			gData[i] = GetRand(1, MAX_RANDOM_NUMBER);
	}
}

// Write a function that calculates the right indices to divide the array into thrdCnt equal divisions
// For each division i, indices[i][0] should be set to the division number i,
// indices[i][1] should be set to the start index, and indices[i][2] should be set to the end index
void CalculateIndices(int arraySize, int thrdCnt, int indices[MAX_THREADS][3]) {
	int section = (arraySize / thrdCnt);
	
	for (int i = 0; i < thrdCnt; i++)
	{
		indices[i][0] = i;
		indices[i][1] = section * i;
		indices[i][2] = (section * (i + 1) ) - 1;
	}
}

// Get a random number in the range [x, y]
int GetRand(int x, int y) {
    int r = rand();
    r = x + r % (y-x+1);
    return r;
}

long GetMilliSecondTime(struct timeb timeBuf){
	long mliScndTime;
	mliScndTime = timeBuf.time;
	mliScndTime *= 1000;
	mliScndTime += timeBuf.millitm;
	return mliScndTime;
}

long GetCurrentTime(void){
	long crntTime=0;
	struct timeb timeBuf;
	ftime(&timeBuf);
	crntTime = GetMilliSecondTime(timeBuf);
	return crntTime;
}

void SetTime(void){
	gRefTime = GetCurrentTime();
}

long GetTime(void){
	long crntTime = GetCurrentTime();
	return (crntTime - gRefTime);
}

