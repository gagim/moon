import 'package:flutter/material.dart';

class TelaPrincipal extends StatefulWidget {
  @override
  _TelaPrincipalState createState() => _TelaPrincipalState();
}

class _TelaPrincipalState extends State<TelaPrincipal> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: Colors.blue,
      ),
      body: Stack(
        children: <Widget>[

        Image.asset("img/back.jpg",fit: BoxFit.fill,height: 1000.0,width: 1000.0),

      ],),

      floatingActionButton: FloatingActionButton(
        backgroundColor: Colors.lightBlue,
          child: Icon(Icons.add,color: Colors.white,),
          onPressed: (){

          }),
    );
  }
}
