import 'package:flutter/material.dart';
import 'loading.dart';
import 'loading2.dart';
import 'post.dart';
import 'post_card.dart';
import 'package:like_button/like_button.dart';
import 'package:http/http.dart';
import'dart:convert';
import 'dart:async';
//Code for lists

class PostDetails extends StatefulWidget {
  @override
  _PostDetailsState createState() => _PostDetailsState();
}

class _PostDetailsState extends State<PostDetails> {
/*
  Future getPostData() async {
    Response response = await get(Uri.parse('https://jsonplaceholder.typicode.com/comments'));
    var data = jsonDecode(response.body);
    List <Comment> comments = [];

    for(var u in data){
      Comment comment = Comment(u['title'],u['body']);
      comments.add(comment);
    }

    return comments;
  }
*/

  Map data = {};
  final double buttonSize = 20.0;
  bool isLiked = false;
  int likeCount = 0;
  @override
  Widget build(BuildContext context) {
    data = ModalRoute.of(context).settings.arguments as Map;
    print('Inside of Post Details frist print');
    print(data);
    return Scaffold(
      backgroundColor: Colors.grey[900],
      appBar: AppBar(
        title: Text('💡'),
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
          FlatButton.icon(
            textColor: Colors.white,
            onPressed: () {
              Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => Loading2(name: data['name'],email: data['email'],userId: data['userId'],)));
            },
            label: Text(''),
            shape: CircleBorder(side: BorderSide(color: Colors.transparent)),
            icon: Icon(Icons.account_circle),
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.fromLTRB(30.0, 40.0, 30.0, 0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text(
              'Title',
              style: TextStyle(
                color: Colors.grey,
                letterSpacing: 2.0,
              ),
            ),
            SizedBox(height: 10.0),
            Text(
              data['title'],
              style: TextStyle(
                color: Colors.amberAccent[200],
                fontWeight: FontWeight.bold,
                fontSize: 20.0,
                letterSpacing: 2.0,
              ),
            ),
            SizedBox(height: 10.0),
            Text(
              'Idea',
              style: TextStyle(
                color: Colors.grey,
                letterSpacing: 2.0,
              ),
            ),
            SizedBox(height: 10.0),
            Text(
              data['body'],
              style: TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.bold,
                fontSize: 10.0,
                letterSpacing: 2.0,
              ),
            ),
            SizedBox(height: 10.0),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: <Widget>[
                LikeButton(
                  size: buttonSize,
                  isLiked: isLiked,
                  likeCount: likeCount,
                  likeBuilder: (isLiked) {
                    final color = isLiked? Colors.blue: Colors.grey;

                    return Icon(Icons.thumb_up, color: color, size: buttonSize);
                  },
                  countBuilder: (count, isLiked, text) {
                    final color = Colors.grey;

                    return Text(
                      text,
                        style: TextStyle(
                        color: color,
                        fontSize: 13,
                        fontWeight: FontWeight.bold,
                      ),
                    );
                  },
                  onTap: (isLiked) async {
                    this.isLiked = !isLiked;
                    this.likeCount += this.isLiked ? 1: -1;
                    //server request Post
                    return !isLiked;
                  },
                ),
                LikeButton(
                  size: buttonSize,
                  isLiked: isLiked,
                  likeCount: likeCount,
                  likeBuilder: (isLiked) {
                    final color = isLiked? Colors.blue: Colors.grey;

                    return Icon(Icons.thumb_down, color: color, size: buttonSize);
                  },
                  countBuilder: (count, isLiked, text) {
                    final color = Colors.grey;

                    return Text(
                      text,
                      style: TextStyle(
                        color: color,
                        fontSize: 13,
                        fontWeight: FontWeight.bold,
                      ),
                    );
                  },
                ),
              ],
            ),
            Divider(
              color: Colors.grey[800],
              height: 60.0,
            ),
            Text(
              data['Comments: '],
              style: TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.bold,
                fontSize: 10.0,
                letterSpacing: 2.0,
              ),
            ),
            SizedBox(height: 10.0),

          ],
        ),
      ),
    );
  }
}//end of class

class Comment{
  final String title, body;
  Comment(this.title, this.body, );
}

