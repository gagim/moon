import 'package:financas/TelaPrincipal.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() async{
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp,
  DeviceOrientation.portraitDown]).then((_){

    MaterialApp(

      home: TelaPrincipal(),
      debugShowCheckedModeBanner: false,
      title: "Finanças",

      theme: ThemeData(
          hintColor: Colors.lightBlue,
              primaryColor: Colors.blue,
      ),

    );

  });
}