import 'package:flutter/material.dart';
import 'package:flutter_app2/pages/post_details.dart';
import 'package:flutter_app2/services/post_info.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'dart:async';

class LoadingPost extends StatefulWidget {
  int id;
  int userId;
  LoadingPost({ this.userId, this.id});
  @override
  _LoadingPostState createState() => _LoadingPostState(userId,id);
}

class _LoadingPostState extends State<LoadingPost> {

  int id, userId;
  _LoadingPostState(this.userId, this.id);

  void setupPostInfo() async {
    String name, email, title,body;
    int id;
    UserInfo userInstance = UserInfo(url: 'users/$userId');
    PostInfo postInstance = PostInfo(url: 'posts/$id');
    await postInstance.getPostInfo();
    await userInstance.getUserInfo();
    print(postInstance.title);
    print(postInstance.body);
    Navigator.pushReplacementNamed(context, '/post_details', arguments: {
      'name': userInstance.name,
      'email': userInstance.email,
      'title': postInstance.title,
      'body': postInstance.body,
      'id': postInstance.id,
    });

  }

  @override
  void initState() {
    super.initState();
    print('Im in here 555');
    setupPostInfo();
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
        backgroundColor: Colors.blue[900],
        body: Center(
            child: SpinKitFadingCube(
              color: Colors.white,
              size: 50.0,
            )
        )
    );
  }

}