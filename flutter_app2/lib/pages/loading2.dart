import 'package:flutter/material.dart';
import 'package:flutter_app2/services/user_info.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'dart:async';

class Loading2 extends StatefulWidget {
  int userId;
  String name, email;
  Loading2({this.name,this.email, this.userId});
  @override
  _Loading2State createState() => _Loading2State(name,email,userId);
}

class _Loading2State extends State<Loading2> {
  int userId;
  String name, email;
  _Loading2State(this.name,this.email, this.userId);


  void setupUserInfo() async {

    Navigator.pushReplacementNamed(context, '/profile2', arguments: {
      'name': name,
      'email': email,
    });
  }

  @override
  void initState() {
    super.initState();
    setupUserInfo();
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
