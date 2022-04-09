import 'dart:async';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;

List<Album> albumFromJson(String str) =>
    List<Album>.from(json.decode(str).map((x) => Album.fromJson(x)));

String albumToJson(List<Album> data) =>
    json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

void main() {
  runApp(const MyApp());
}

//this class builds the app with the messageList information
class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'The Buzz',
      theme: ThemeData(primarySwatch: Colors.blueGrey, accentColor: Colors.red),
      home: messageList(title: 'The Buzz'),
    );
  }
}

//This is where the home page is made
class messageList extends StatefulWidget {
  final String title;

  messageList({Key? key, required this.title}) : super(key: key);

  @override
  State<messageList> createState() => _messageListState();
}

//extends messageList
class _messageListState extends State<messageList> {
  //here I initialized the array that holds the JSON data and where I define the variable for the url
  final url =
      'postgres://rnekgftpvgjpkg:6ff4064ce148c71d5b57b60242f91bd09a3d41eb6222f575da2d0aa4796bb7de@ec2-23-23-128-222.compute-1.amazonaws.com:5432/d8q2nctgjjbrv2';
  var postsJson = [];

  //Here is where I build the widgets that display all the information
  @override
  Widget build(BuildContext context) {
    List likeList = [];
    List dislikeList = [];
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Container(
        child: FutureBuilder(
            future: fetchAlbum(),
            builder: (BuildContext context, AsyncSnapshot snapshot) {
              print(snapshot.data);
              if (snapshot.data == null) {
                return Container(
                    child: Center(
                  child: Text("There are no posts yet..."),
                ));
              } else {
                return ListView.builder(
                    itemCount: snapshot.data.length,
                    itemBuilder: (BuildContext, int index) {
                      return ListTile(
                        leading: CircleAvatar(
                          child: Text(snapshot.data[index].score.toString()),
                        ),
                        title: Text(snapshot.data[index].username +
                            ": " +
                            snapshot.data[index].title),
                        subtitle: Text(snapshot.data[index].body),
                        trailing: PopupMenuButton(
                          //this itemBuilder creates the popup menu to like or dislike an idea
                          itemBuilder: (context) {
                            return [
                              const PopupMenuItem(
                                value: 'Like',
                                child: Text('Like'),
                              ),
                              const PopupMenuItem(
                                value: 'Dislike',
                                child: Text('Dislike'),
                              ),
                            ];
                          },
                          //when you select like or dislike, it updates the score of that idea
                          onSelected: (String value) {
                            setState(() {
                              int newScore;
                              newScore = affinity(
                                  value,
                                  snapshot.data[index].score,
                                  snapshot.data[index].body,
                                  likeList,
                                  dislikeList);
                              putLikes(url, newScore);
                            });
                          },
                        ),
                      );
                    });
              }
            }),
      ),
      //This floating action button takes you to a second screen where you can write a new post
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          var newMessage = await Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => postScreen()),
          );
          //setState updates the screen to display the new idea
          setState(() {
            postAlbum(url, 'default', newMessage[0], newMessage[1], 0);
          });
        },
        child: Text('Post'),
        backgroundColor: Colors.blue,
      ),
    );
  }
}

//This class is where I built the second screen where you can post your idea
class postScreen extends StatefulWidget {
  postScreen({Key? key}) : super(key: key);

  @override
  State<postScreen> createState() => _postScreenState();
}

//This extends the postScreen
class _postScreenState extends State<postScreen> {
  List newMessage = ['', ''];
  //This builds the lines where you can write the title and messages
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Post Your Idea"),
        ),
        body: Center(
          child: Column(mainAxisSize: MainAxisSize.min, children: [
            TextField(
              decoration: InputDecoration(labelText: 'Title'),
              onChanged: (val) => newMessage[0] = val,
            ),
            TextField(
              decoration: InputDecoration(labelText: 'Message'),
              onChanged: (val) => newMessage[1] = val,
            )
          ]),
        ),
        //This confirms your post and brings you back to the first screen
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            Navigator.pop(context, newMessage);
          },
          child: Text('Post'),
          backgroundColor: Colors.blue,
        ));
  }
}

Future<List<Album>> fetchAlbum() async {
  final response = await http.get(Uri.parse(
      'postgres://rnekgftpvgjpkg:6ff4064ce148c71d5b57b60242f91bd09a3d41eb6222f575da2d0aa4796bb7de@ec2-23-23-128-222.compute-1.amazonaws.com:5432/d8q2nctgjjbrv2/ideas'));

  var album = jsonDecode(response.body);
  List<Album> albums = [];
  for (var i in album) {
    Album album = Album(
        username: i['username'],
        title: i['title'],
        body: i['body'],
        score: i['score']);
    albums.add(album);
  }
  print(albums.length);
  return albums;
}

class Album {
  Album({
    required this.username,
    required this.title,
    required this.body,
    required this.score,
  });

  String username;
  String title;
  String body;
  int score;

  factory Album.fromJson(Map<String, dynamic> json) => Album(
        username: json["username"],
        title: json["title"],
        body: json["body"],
        score: json["score"],
      );

  Map<String, dynamic> toJson() => {
        "username": username,
        "title": title,
        "body": body,
        "score": score,
      };
}

//This is the method that posts the new idea to the backend
Future<List<Album>> postAlbum(
    String url, String username, String title, String body, int score) async {
  final response = await http.post(Uri.parse(url + '/ideas'), headers: {
    'Content-Type': 'application/json',
    'Charset': 'utf-8'
  }, body: {
    'username': username,
    'title': title,
    'body': body,
    'score': score.toString(),
  });

  var album = jsonDecode(response.body);
  print(album);
  return album;
}

//This is the method that updates the like count on the backend
Future<List<Album>> putLikes(String url, int score) async {
  final response = await http.post(Uri.parse(url), body: {
    "score": score,
  });
  var album = jsonDecode(response.body);
  return album;
}

//this increments and deincrements likes and ensures you can't like the same thing twice
int affinity(
    String value, int likeCount, String body, List likeList, List dislikeList) {
  if (value == 'Like') {
    for (int n = 0; n < likeList.length; n++) {
      if (body = likeList[n]) {
        likeCount++;
        likeList.add(body);
      }
    }
  } else if (value == 'Dislike') {
    for (int n = 0; n < dislikeList.length; n++) {
      if (body = dislikeList[n]) {
        likeCount--;
        dislikeList.add(body);
      }
    }
  }
  return likeCount;
}
