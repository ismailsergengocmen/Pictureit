# CS102 ~ Personal Log page ~

## Ahmed Salih Cezayir 

On this page I will keep a weekly record of what I have done for the CS102 group project. This page will be submitted together with the rest of the repository, in partial fulfillment of the CS102 course requirements.

### ~ 20/04/2020 ~
This week I started to learn android studio and concepts related to android programing.

### ~ 27/04/2020 ~
This week I will continue my study of how to use android studio
I will think about the UML diagram of our app
I will study how to use Firebase or another database (if there is a problem with Firebase)  

### ~ 04/05/2020 ~
This week I designed the basic layout of the app
I created home, search and profile menu layout and added some basic functionality like moving between different activities. I also worked with firebase and learned how to use authentication etc.

### ~ 10/05/2020 ~
This week I worked with photoes. I learned how to get camera permission, how to capture images and how to upload those images to firebase storage. I also worked with progress map section and created some basic layout for that section. I created a custom adapter and custom recyclerView and those will be added to the project when everyhing is ready. Also this week Sergen and I worked worked together to solve some problems related to authentication and sign in / sign out part. I also tidied up code layouts to make them more readible and nicer looking. Next week I will work on implementing those ideas/knowledge into our project.

### ~ 16/05/2020 ~
This week I added the camera functionality to our app. I also worked with firebase database and firebase storage. I added the functionality that now our app can store the captured image and its url in the firebase. I also added camera and write/read external data permissions in order for our app to work. Now we need to combine this part and Faruk's part in order to complete the tasks page.-

### ~ 17/05/2020 ~
Today I completed the progressmap. I added CardRecyclerViewAdapter class(Custon adapter class for recylerview), layaout_cardview (xml) and fragment_progressmap(xml). Then I filled the progressMap fragment by using those (I used the firebase in order to get the photos). Now we can see the photos taken and their date

### ~ 18/05/2020 ~
Today I created the search activity. I added activity_search(xml) and snippet_searchbar(xml). Then I filled the search activity.(I used the firebase in order to get the related photos) Now user can enter a tag and search for related photos.Then, I organized our database and storage. With my group we decided the basic structure of the database. Then I made it. I added 2 new nodes(all_photos and tags_and_photos). I also organized the storage. Now storage have 2 folders(images and all_images). Then I organized and added the related methods to add photo to storage and realtime database(Those methods are uploading photos into correct folders/nodes). Then I tidied some parts of the project. 


### ~ 19/05/2020 ~
Today I worked with Sergen to solve a problem related to profile activity. We solved the problem. Then I worked with badge/achievements system. Now it is correct and it properly shows the achievement/ achievement badge/ progress/ wheter it is done or not. I also added functionality to the profile activity that now users can see their photo count.(Now users can see how many pictures they have captured).