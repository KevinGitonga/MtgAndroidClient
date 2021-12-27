# MtgAndroidClient

Android Client Application consuming Magic The Gathering Apis https://docs.magicthegathering.io/

#Tools
- Android studio Arctic Fox | 2020.3.1. 4.3
- Postman

#Implementation Notes
- Project has been implemented using MVVM model.
- Data from sets endpoint (/sets) and cards endpoint (/cards) is paginated so i leverage the Pagination Api
https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data for the other pages data is loaded through normal process.
- Data is cached in viewmodels for the Paged data, while saved state handler caches data for the random booster Pack, this prevents auto reloading
during back navigation.

- The first screen shows sets from Api.
- When user clicks on a given set they are given option to either lists cards for given set or generate random booster for given set.
- When user clicks on a card thay are able to see details about the clicked card.
