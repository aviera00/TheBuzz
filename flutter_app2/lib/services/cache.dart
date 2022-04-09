import 'package:path_provider/path_provider.dart';



Class Cache{

//needs to check if file exists
    Future<String> getFilePath() async {
    Directory appDocumentsDirectory = await getApplicationDocumentsDirectory(); 
    String appDocumentsPath = appDocumentsDirectory.path; 
    String filePath = '$appDocumentsPath/store.txt'; 

    return filePath;
  }

    void writeIn(Post p) async {
    File file = File(await getFilePath()); 
    var buffer = new StringBuffer();
    buffer.write(p.title);
    buffer.write(",");
    buffer.write(p.body);
    buffer.write(",");
    buffer.write(p.userId.toString());
    buffer.write(",");
    buffer.write(p.id.toString());
    buffer.write(";");
    file.writeAsString(edcode(buffer.toString()),mode: FileMode.append); 
  }

  List<Post> read() async {
    File file = File(await getFilePath()); 
    String fileContent = await file.readAsString(); 
    List<Post> pl = []
    Var posts = fileContent.spilt("``")
    for (var s in posts){
      var attri = (decode(s)).split(",")
      Post post = Post(attri[2],attri[3],attri[0],attri[1],);
      pl.add(post)
    }
    return pl;
}

  String encode(String input) async {
    List<int> bytes = utf8.encode(foo);
    var buffer = new StringBuffer();
    for (int b in bytes){
      buffer.write(b.toString());
      buffer.write("`");
    }
      buffer.write("`");
      return buffer.toString();
  }

  String decode(String input) async {
    List splitnums = input.split('`').map(int.parse).toList(); 
    return utf8.decode(splitnums);
  }


}