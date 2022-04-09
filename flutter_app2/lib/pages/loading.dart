import 'package:flutter/material.dart';
import 'package:flutter_app2/services/user_info.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
class Loading extends StatefulWidget {
 
  @override
  _LoadingState createState() => _LoadingState();
}

class _LoadingState extends State<Loading> {

  void setupUserInfo() async {
    UserInfo instance = UserInfo(url: 'users/1');
    await instance.getUserInfo();
    Navigator.pushReplacementNamed(context, '/profile', arguments: {
      'name': instance.name,
      'email': instance.email,
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
