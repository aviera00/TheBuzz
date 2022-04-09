import 'package:http/http.dart';
import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'dart:convert';


class UserInfo {

  String name; //
  String url; //
  String email; //

  UserInfo({this.url});



  Future<void> getUserInfo() async {

    try{
      // make the request
      Response response = await get(Uri.parse('https://jsonplaceholder.typicode.com/$url'));
      Map data = jsonDecode(response.body);

      // set properties from json
      name = data['name'];
      email = data['email'];
      print('Hello1111111666666666666');
      print(name);

    }
    catch (e) {
      print(e);
      name = 'could not get name';
    }

  }

}

class PostInfo {

  String title;
  String body;
  String url; //
  int id;

  PostInfo({this.url});



  Future<void> getPostInfo() async {
    print('Im in here!');
    try{
      // make the request
      Response response = await get(Uri.parse('https://jsonplaceholder.typicode.com/$url'));
      Map data = jsonDecode(response.body);

      // set properties from json
      title =  data['title'];
      body = data['body'];
      id = data['id'];
      print('________________________________');
      print(title);
    }
    catch (e) {
      print(e);
      title = 'could not get title';
    }

  }

}