# UserRetrofit
Android application for adding new user to Firebase database.
User can be added on button clicked after edit field are correctly entered.
User can be deleted on button clicked, on click ImageView 'trash' clicked will be open confirmation dialog that asks user if he/she is sure to delete selected item(user).
Finally on long click item from RecyclerView user will be automatically deleted from list.

Users are sorted automatically after adding new user by lastname and firstname.

Components used in Application:
 - Retrofit2(https://square.github.io/retrofit/)
 - Firebase(https://firebase.google.com)
 - Gson(https://github.com/google/gson)
 - Recyclerview and Cardview
 - ButterKnife(https://jakewharton.github.io/butterknife/)
