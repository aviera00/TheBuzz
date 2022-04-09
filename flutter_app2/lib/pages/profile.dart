import 'package:flutter/material.dart';
import 'dart:async';

class Profile extends StatefulWidget {
  @override
  State<Profile> createState() => _ProfileState();
}

class _ProfileState extends State<Profile> {
  //List<Post> postList = List();
  //List<Comment>
  //String author = this.author;
  //String email = this.email
  Map data = {};

  @override
  Widget build(BuildContext context) {
    data=ModalRoute.of(context).settings.arguments as Map;

    print(data);

    return Scaffold(
      backgroundColor: Colors.grey[900],
      appBar: AppBar(
        title: Text('Profile'),
        centerTitle: true,
        backgroundColor: Colors.grey[850],
        elevation: 0.0,
        actions: <Widget>[
          FlatButton.icon(
            textColor: Colors.white,
            onPressed: () {
              Navigator.pushNamed(context, '/home');
            },
            label: Text(''),
            shape: CircleBorder(side: BorderSide(color: Colors.transparent)),
            icon: Icon(Icons.home),
          ),
            FlatButton.icon2(
            textColor: Colors.white,
            onPressed: () {
              Navigator.pushNamed(context, '/camera');
            },
            label: Text('take photo'),
            shape: CircleBorder(side: BorderSide(color: Colors.transparent)),
            icon: Icon(Icons.camera_front),
          ),
            FlatButton.icon3(
            textColor: Colors.white,
            onPressed: () {
              Navigator.pushNamed(context, '/galleryAccess');
            },
            label: Text('look photo'),
            shape: CircleBorder(side: BorderSide(color: Colors.transparent)),
            icon: Icon(Icons.camera_enhance),
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.fromLTRB(30.0, 40.0, 30.0, 0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Center(
              child: CircleAvatar(
                radius: 40.0,
                backgroundImage: AssetImage('assets/canyons.jpg'),
              ),
            ),
            Divider(
              color: Colors.grey[800],
              height: 60.0,
            ),
            Text(
              'NAME',
              style: TextStyle(
                color: Colors.grey,
                letterSpacing: 2.0,
              ),
            ),
            SizedBox(height: 10.0),
            Text(
              data['name'],
              style: TextStyle(
                color: Colors.amberAccent[200],
                fontWeight: FontWeight.bold,
                fontSize: 28.0,
                letterSpacing: 2.0,
              ),
            ),
            SizedBox(height: 30.0),
            Row(
              children: <Widget>[
                Icon(
                  Icons.email,
                  color: Colors.grey[400],
                ),
                SizedBox(width: 10.0),
                Text(
                  data['email'],
                  style: TextStyle(
                    color: Colors.grey[400],
                    fontSize: 18.0,
                    letterSpacing: 1.0,
                  ),
                )
              ],
            ),
            SizedBox(height: 30.0),
            Text(
              'ACTIVITY: ',
              style: TextStyle(
                color: Colors.grey,
                letterSpacing: 2.0,
              ),
            ),
            Row(
              children: <Widget>[
                Icon(
                  Icons.comment,
                  color: Colors.grey[400],
                ),
                SizedBox(width: 10.0),
                Text(
                  'Sample comment',
                  style: TextStyle(
                    color: Colors.grey[400],
                    fontSize: 18.0,
                    letterSpacing: 1.0,
                  ),
                )
              ],
            ),
            Row(
              children: <Widget>[
                Icon(
                  Icons.lightbulb_outline,
                  color: Colors.grey[400],
                ),
                SizedBox(width: 10.0),
                Text(
                  'Sample Idea',
                  style: TextStyle(
                    color: Colors.grey[400],
                    fontSize: 18.0,
                    letterSpacing: 1.0,
                  ),
                )
              ],
            ),
          ],
        ),
      ),
    );
  }
}//stateless widget UserProfile