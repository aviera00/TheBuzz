import 'package:flutter/material.dart';
import 'loading_post.dart';
import 'post.dart';
import 'post_card.dart';
import 'package:postgrest/postgrest.dart';
import 'package:http/http.dart';
import'dart:convert';
import 'package:path_provider/path_provider.dart';
import 'dart:io';
//Code for lists

class Home extends StatefulWidget {
  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {

  Future getPostData() async {
    Response response = await get(Uri.parse('https://jsonplaceholder.typicode.com/posts'));
    var data = jsonDecode(response.body);
    List <Post> posts = cache.read();
    for(var u in data){
      Post post = Post(u['userId'], u['id'],u['title'],u['body']);
      if (!posts.contains(post)){
      posts.add(post);
      cache.writeIn(post);
      }
    }
    return posts;
  }


  @override
  Widget build(BuildContext context) {
    int userId;
    int id;
    return Scaffold(
      backgroundColor: Colors.grey[900],
      appBar: AppBar(
        title: Text('Ideas Home'),
        centerTitle: true,
        backgroundColor: Colors.grey[850],
      ),
      body: Container(
            child: FutureBuilder(
            future: getPostData(),
            builder: (context, snapshot) {
              if (snapshot.data == null) {
                return Container(
                  child: Center(
                    child: Text('Loading'),
                  ),
                );
              }else{
                return ListView.builder(
                    itemCount: snapshot.data.length,
                    itemBuilder: (context, i) {
                      return Padding(
                        padding: const EdgeInsets.symmetric(vertical: 1.0, horizontal: 4.0),
                        child: Card(
                          child: ListTile(
                            onTap: () {
                              print(snapshot.data[i].userId);
                              print(snapshot.data[i].id);
                              Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => LoadingPost(userId: snapshot.data[i].userId, id:snapshot.data[i].id )));
                              /*
                              Navigator.pushReplacementNamed(context, '/loading_post', arguments: {
                                'userId': snapshot.data[i].userId,
                                'id': snapshot.data[i].id,
                              });
                              */

                            },
                            title: Text(snapshot.data[i].title),
                            subtitle: Text(snapshot.data[i].body),
                          ),
                        ),
                      );
                    }
                );
              }
            },
          ),
      ),
    );
  }

}//home state end


  
    class Post{
      final String title, body;
      final int userId, id;
      Post(this.userId, this.id, this.title, this.body, );
    }
