## Zattoo - Android Code Challenge


## Overview
MVVM design architecture with clean architecture so the project be testable and scalable for new features.
Dagger hilt, DataBinding, MockK, LiveData and Coroutine.


## decisions made while refactoring

- Remove logic from UI apply separation of concerns to unit test business logics
- MVVM design architecture to hold UI state
- ViewModel must be 100% free of Android framework code
- Coroutine for concurrency
- Dagger Hilt for less boilerplate code
- UI state enum and home state object to update the UI for less boilerplate and readability.
- DataBinding the recyclerView for cleaner fragment
- Unregister listeners to avoid memory leaks
- Apply DRY and SOLID principles