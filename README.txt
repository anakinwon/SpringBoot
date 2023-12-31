스프링 배치 - Spring Boot 기반으로 개발하는 Spring Batch

총 110개 수업 (45시간 14분)


초급자를 위해 준비한
[웹 개발, 백엔드] 강의입니다.
초급에서 중~고급에 이르기까지 스프링 배치의 기본 개념부터 API 사용법과 내부 아키텍처 구조를 심도있게 다룹니다.
그리고 스프링 배치 각 기능의 흐름과 원리를 학습하게 되고 이를 바탕으로 다양한 배치 어플리케이션 개발을 위한 실무적 감각을 익히게 됩니다.


<✍️ 이런 걸 배워요!>

스프링 배치 핵심 도메인 이해
스프링 배치 Job, Step, Flow 이해 및 API 활용
스프링 배치 Chunk 기반 프로세스 이해 및 활용
스프링 배치 예외 및 오류 제어
스프링 배치 멀티 스레드 프로세싱 이해 및 활용
스프링 배치 이벤트 리스너 다루기
스프링 배치 TDD 및 운영 API 활용
스프링 배치 실전 프로젝트



<학습 커리큘럼>
1. 스프링 배치의 핵심 도메인 이해 및 활용
스프링 배치에서 Job을 구성하는데 있어서 사용되는 여러 도메인들이 있습니다.
예를 들어 Job, Step, Flow, Tasklet, JobInstance, Jobexecution, StepExecution, ExecutionContext 등..
이러한 도메인들에 대한 개념부터 확실하게 이해를 해야 올바른 Job을 구성하고 활용할 수 있습니다. 각 도메인들의 용어적 개념과
도메인들간의 관계를 이해함으로써 간단한 Job부터 복잡한 Job까지 원하는 Job을 체계적으로 구성하는 방법을 익히게 됩니다.

2. 스프링 배치의 Job, Step, Flow 이해 및 API 활용
스프링 배치에서 Job을 구성하기 위한 기본적인 단위와 항목들에 대해서 학습합니다. Job, Step, Flow의 기본 개념과 구조를 이해하고
각 API 에 대한 설정과 활용방법에 대해 학습합니다. 그리고 Job과 Step의 여러 유형들에 대해 살펴보고 Job, Step, Flow를 조합하여
배치잡을 구성하는 방법을 학습함으로서 스프링 배치의 가장 기본이 되는 Job의 구성 및 활용을 자유자재로 구현할 수 있는 능력을 기르게 됩니다.


3. 스프링 배치의 청크 기반 프로세싱 정복
스프링 배치에서 가장 핵심적인 기능 중에 하나가 바로 청크 기반 프로세싱입니다. Chunk 개념을 도입하여 대용량의 데이터를
고성능으로 처리를 할 수 있도록 합니다. 여기에 사용되는 API가 ItemReader, ItemProcessor, ItemWriter 입니다.
청크 기반 프로세싱의 기본적인 개념과 원리를 학습하고 내부 아키텍처까지 파악합니다. 그리고 다양한 예제를 통해 청크 기반 프로세싱을 통한
데이터 처리의 활용법을 정복하게 됩니다.


4. 스프링 배치의 멀티 스레드 프로세싱 정복
대용량의 데이터 처리와 시간이 많이 소요되는 배치 처리는 단일 스레드가 아닌 멀티 스레드로 구성하여 동시에 병렬적인 배치 처리를 함으로써
더욱 효율적인 배치 처리가 이루어지도록 합니다. 자바의 스레드 모델에 대한 기본적인 개념과 스프링 배치에서 제공하는
멀티 스레드 관련된 기술들을 먼저 이해하고 여러 멀티 스레드 유형의 배치처리 기술들을 익히게 됩니다.


5. 스프링 배치의 오류 제어
배치 실행에 있어서 오류나 예외는 언제든지 발생할 가능성이 있습니다. 이러한 상황에서 오류로 인한 장애를 미리 예상하고 대비함으로써
배치 서비스가 완전히 중단되는 것이 아닌 일시적인 중단 혹은 예외를 무시하고 다음 단계로 가는 등의 처리를 함으로써 내결함성을 가진 배치
어플리케이션을 어떻게 구성할 수 있는지 학습하게 됩니다. 이와 관련된 기술인 Skip과 Retry 기능에 대한 자세한 내용과 실습을 진행합니다.


6. 실전! 스프링 배치 어플리케이션 예제
스프링 배치의 기술을 사용해서 실전에서 응용할 수 있는 배치 어플리케이션을 제작하는 시간을 가집니다.
스프링 배치 기반 위에서 멀티 스레드 구조로 API 서버와 통신하는 배치 어플리케이션을 구현하는 예제를 구현해 봄으로써
스프링 배치의 전반적인 내용을 이해함과 동시에 실무에서 스프링 배치를 어떻게 활용할 수 있는지에 대한 식견을 넓혀 주는 시간이 될 것입니다.




애플리케이션으로 실행하는 방식 :
D:\JpaNQueryDsl\springbatch\build\libs>
    java -jar springbatch-0.0.1-SNAPSHOT.jar name=anakin seq(LONG)=2L creatdDt(date)=2023-06-20 age(double)=3.14