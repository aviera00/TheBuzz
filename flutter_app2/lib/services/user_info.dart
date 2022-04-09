import 'package:http/http.dart';
import 'dart:convert';


class UserInfo {

  String name; //
  String email; //
  String url; //

  UserInfo({this.url});

  Future<void> getUserInfo() async {

    try{
      // make the request
      Response response = await get(Uri.parse('https://jsonplaceholder.typicode.com/$url'));
      Map data = jsonDecode(response.body);

      // set properties from json
       name = data['name'];
       email = data['email'];
    }
    catch (e) {
      print(e);
      name = 'could not get name';
      email = 'could not get email';
    }

  }

}