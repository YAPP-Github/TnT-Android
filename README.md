# TnT (Trainer & Trainee)  

> 트레이너, 트레이니를 위한 PT 통합 관리 서비스

<img width="2267" alt="TnT 2" src="https://github.com/user-attachments/assets/ce3d3242-8d7d-4b0d-a4b5-565d65ce9cc5" />

## 🛠️ Spec
- `Clean Architecture`
- `MVI`
- `Hilt`
- `Coroutines`
- `Jetpack Compose`
- `Retrofit2`
- `DataStore`
- `Serialization`
- `Coil`
- `FCM`
- `Ktlint`, `Detekt`

## 🚙 Navigation Structure
```mermaid
flowchart TD
    Login[Login]
    RoleSelection[RoleSelection]
    
    TraineeSignUp[TraineeSignUp]
    TrainerSignUp[TrainerSignUp]
    
    TraineeConnect[TraineeConnect]
    TrainerInvite[TrainerInvite]
    TrainerConnect[TrainerConnect]
    
    TraineeMain[TraineeMain]
    TrainerMain[TrainerMain]
    
    subgraph "Trainee Routes"
        TraineeHome[TraineeMainTab.Home]
        TraineeMyPage[TraineeMainTab.MyPage]
        TraineeMealRecord[TraineeMealRecord]
        TraineeMealDetail[TraineeMealDetail]
    end
    
    subgraph "Trainer Routes"
        TrainerHome[TrainerMainTab.Home]
        TrainerMembers[TrainerMainTab.Members]
        TrainerFeedback[TrainerMainTab.Feedback]
        TrainerMyPage[TrainerMainTab.MyPage]
        AddPtSession[AddPtSession]
    end
    
    Login --> RoleSelection
    RoleSelection --> TraineeSignUp
    RoleSelection --> TrainerSignUp
    
    TraineeSignUp --> TraineeConnect
    TraineeConnect --> TraineeMain
    
    TrainerSignUp --> TrainerInvite
    TrainerConnect --> TrainerMain
    
    TrainerInvite --> TrainerMain
    
    TraineeMain --> TraineeHome
    TraineeMain --> TraineeMyPage
    TraineeHome --> TraineeMealRecord
    TraineeMealRecord --> TraineeMealDetail
    
    TrainerMain --> TrainerHome
    TrainerMain --> TrainerMembers
    TrainerMain --> TrainerFeedback
    TrainerMain --> TrainerMyPage
    TrainerHome --> AddPtSession
```


## 📦 Package Structure
```
App
├── build-logic/                   
├── domain/                        
├── data/                          
│   ├── network/  
│   ├── storage/  
│   ├── repository/  
│   └── session/  
├── core/                          
│   ├── designsystem/  
│   ├── navigation/  
│   ├── ui/  
│   └── login/  
├── feature/                       
│   ├── main/  
│   ├── login/  
│   ├── roleselect/  
│   ├── webview/  
│   ├── trainer/                   
│   │   ├── signup/  
│   │   ├── connect/  
│   │   ├── invite/  
│   │   ├── main/  
│   │   ├── home/  
│   │   ├── feedback/  
│   │   ├── members/  
│   │   ├── mypage/  
│   │   ├── notification/  
│   │   ├── addptsession/  
│   │   └── modifymyinfo/  
│   └── trainee/                   
│       ├── signup/  
│       ├── connect/  
│       ├── main/  
│       ├── home/  
│       ├── mypage/  
│       ├── notification/  
│       ├── mealrecord/  
│       ├── mealdetail/
│       └── modifymyinfo/
└── gradle/  
    └── libs.versions.toml         
```
