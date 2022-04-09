import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_app2/pages/home.dart';
import 'package:flutter_app2/pages/loading_post.dart';
import 'package:flutter_app2/pages/login_page.dart';
import 'package:flutter_app2/pages/post_details.dart';
import 'package:flutter_app2/pages/profile.dart';
import 'package:flutter_app2/pages/loading.dart';
import 'package:flutter_app2/pages/profile2.dart';
import 'package:flutter_app2/pages/camera.dart';
import 'pages/post.dart';
import 'pages/post_card.dart';
import 'dart:async';
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:camera/camera.dart';
import 'package:localstore/localstore.dart';
import 'package:image_picker/image_picker.dart';
import "package:flutter_app2/pages/GalleryAccessState"

void main() { 




  // Obtain a list of the available cameras on the device.
  final cameras = await availableCameras();

  // Get a specific camera from the list of available cameras.
  final firstCamera = cameras.first;

  
  runApp(MaterialApp(
  initialRoute: '/',
  routes: {
    '/': (context) => Loading(),
    '/home': (context) => Home(),
    '/profile': (context) => Profile(),
    '/post_details': (context) => PostDetails(),
    '/loading_post': (context) => LoadingPost(),
    '/profile2': (context) => Profile2(),
    '/camera': (context) => TakePictureScreen(
        // Pass the appropriate camera to the TakePictureScreen widget.
        camera: firstCamera,
      ), 
    '/gallery': (context) => GalleryAccess(),
  },
));
}












