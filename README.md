# Android-Advanced-Practice
Learning RxJava for Android by example

這是一個使用 Android 使用 RxJava 和 Kotlin 的練習 (主要專注在 Rxjava 應用)，  
目前是參考：https://github.com/kaushikgopal/RxJava-Android-Samples  
將程式轉成 Kotlin 並且使用 kotlin-android-extensions 作 DataBinding  

#### Examples:
1. 背景工作＆Concurrency
2. 累積呼叫次數
3. 搜索詞句監聽 (去除誤打)
4. 雙重綁定 (PublishProcessor)
5. 輪詢（Polling）呼叫＆ RxJava
6. Event Bus ＆ RxJava
7. 表單驗證 （CombineLatest）

#### 案例細節：
1. 背景工作＆Concurrency :Background work & concurrency (using Schedulers)  
這是一個將長時間運行操作放置到後台線程的示範。  
操作完成後，將回到主線程。  
將長時間運行操作放置到後台線程，所以多次點擊按鈕後不會被阻止ui操作。  
(Concurrency 為多個Threads輪流去執行)  

2. 累積呼叫次數：Accumulate calls (using buffer)  
這是一個“緩存”積事件的案例。  
連續多次點擊下面的按鈕，會顯示兩秒內的點擊次數。  

3. 搜索詞句監聽:Instant/Auto searching text listeners (using Subjects & debounce)  
這是一個 搜索詞句 監聽事件的案例。  
並不是每次輸入都會觸發搜尋，而是輸入最後一個詞後才觸發。  

4. 雙重綁定:Two-way data binding for TextViews (using PublishSubject)  
根據輸入自動更新，  
實作雙向綁定，更有效地使用 View Model。(沒有使用 butterknife 所以與原作不同)  

5. 輪詢（Polling）呼叫：Simple and Advanced polling (using interval and repeatWhen)  
這是一個輪詢（Polling）的案例。  
簡單輪詢：顯示後台（模擬）輪流進行網絡呼叫。(概念是，定時發出詢問，依序詢問每一個週邊裝置是否需要其服務，有即給予服務，服務結束後再問下一個週邊，接著不斷週而復始)  

6. Event Bus ＆ RxJava  
這是一個 Event Bus 使用範例  
點擊的按鈕，RxBus 將會監聽到這些事件  

8. 表單驗證 Form validation (using .combineLatest)  
使用 combineLatest 監視多個狀態。當全部輸入欄為有效的時候，提交按鈕自動亮起  
