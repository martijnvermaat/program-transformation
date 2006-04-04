CompilationUnit(
  None
, [ TypeImportOnDemandDec(PackageName([Id("gnu"), Id("bytecode")]))
  , TypeImportOnDemandDec(PackageName([Id("java"), Id("io")]))
  ]
, [ ClassDec(
      ClassDecHead([Public], Id("RTCG4Generated"), None, None, None)
    , ClassBody(
        [ MethodDec(
            MethodDecHead(
              [Public, Static]
            , None
            , Void
            , Id("main")
            , [ Param(
                  []
                , ArrayType(ClassOrInterfaceType(TypeName(Id("String")), None))
                , Id("args")
                )
              ]
            , Some(
                ThrowsDec(
                  [ ClassType(TypeName(Id("IOException")), None)
                  , ClassType(TypeName(Id("NoSuchMethodException")), None)
                  , ClassType(TypeName(Id("IllegalAccessException")), None)
                  , ClassType(
                      TypeName(
                        PackageOrTypeName(
                          PackageOrTypeName(PackageOrTypeName(Id("java")), Id("lang"))
                        , Id("reflect")
                        )
                      , Id("InvocationTargetException")
                      )
                    , None
                    )
                  ]
                )
              )
            )
          , Block(
              [ LocalVarDecStm(
                  LocalVarDec(
                    []
                  , Int
                  , [ VarDec(
                        Id("count")
                      , Invoke(
                          Method(MethodName(AmbName(Id("Integer")), Id("parseInt")))
                        , [ArrayAccess(ExprName(Id("args")), Lit(Deci("0")))]
                        )
                      )
                    ]
                  )
                )
              , LocalVarDecStm(
                  LocalVarDec(
                    []
                  , Int
                  , [VarDec(Id("n"), Lit(Deci("16")))]
                  )
                )
              , LocalVarDecStm(
                  LocalVarDec(
                    []
                  , ClassOrInterfaceType(TypeName(Id("ClassType")), None)
                  , [ VarDec(
                        Id("co")
                      , Invoke(
                          Method(MethodName(Id("GenPowerClass")))
                        , [ExprName(Id("n"))]
                        )
                      )
                    ]
                  )
                )
              , ExprStm(
                  Invoke(
                    Method(MethodName(AmbName(Id("ClassTypeWriter")), Id("print")))
                  , [ ExprName(Id("co"))
                    , ExprName(AmbName(Id("System")), Id("out"))
                    , Lit(Deci("0"))
                    ]
                  )
                )
              , LocalVarDecStm(
                  LocalVarDec(
                    []
                  , ArrayType(Byte)
                  , [ VarDec(
                        Id("classFile")
                      , Invoke(
                          Method(MethodName(AmbName(Id("co")), Id("writeToArray")))
                        , []
                        )
                      )
                    ]
                  )
                )
              , LocalVarDecStm(
                  LocalVarDec(
                    []
                  , ClassOrInterfaceType(TypeName(Id("Class")), None)
                  , [ VarDec(
                        Id("ty")
                      , Invoke(
                          Method(
                            NewInstance(
                              None
                            , ClassOrInterfaceType(TypeName(Id("ArrayClassLoader")), None)
                            , []
                            , None
                            )
                          , None
                          , Id("loadClass")
                          )
                        , [Lit(String([Chars("MyClass")])), ExprName(Id("classFile"))]
                        )
                      )
                    ]
                  )
                )
              , LocalVarDecStm(
                  LocalVarDec(
                    []
                  , ClassOrInterfaceType(
                      TypeName(
                        PackageOrTypeName(
                          PackageOrTypeName(PackageOrTypeName(Id("java")), Id("lang"))
                        , Id("reflect")
                        )
                      , Id("Method")
                      )
                    , None
                    )
                  , [ VarDec(
                        Id("m")
                      , Invoke(
                          Method(MethodName(AmbName(Id("ty")), Id("getMethod")))
                        , [ Lit(String([Chars("MyPower")]))
                          , NewArray(
                              TypeName(Id("Class"))
                            , [Dim]
                            , ArrayInit([Lit(Class(Int))])
                            )
                          ]
                        )
                      )
                    ]
                  )
                )
              , ExprStm(
                  Invoke(
                    Method(
                      MethodName(
                        AmbName(AmbName(Id("System")), Id("out"))
                      , Id("println")
                      )
                    )
                  , [ Invoke(
                        Method(MethodName(AmbName(Id("m")), Id("invoke")))
                      , [ Lit(Null)
                        , NewArray(
                            TypeName(Id("Object"))
                          , [Dim]
                          , ArrayInit(
                              [ NewInstance(
                                  None
                                , ClassOrInterfaceType(TypeName(Id("Integer")), None)
                                , [Lit(Deci("3"))]
                                , None
                                )
                              ]
                            )
                          )
                        ]
                      )
                    ]
                  )
                )
              ]
            )
          )
        , MethodDec(
            MethodDecHead(
              [Public, Static]
            , None
            , ClassOrInterfaceType(TypeName(Id("ClassType")), None)
            , Id("GenPowerClass")
            , [Param([], Int, Id("n"))]
            , None
            )
          , Block(
              [ Return(
                  Some(
                    QuoteClassDec(
                      ClassDec(
                        ClassDecHead([Public], Id("MyClass"), None, None, None)
                      , ClassBody(
                          [ MethodDec(
                              MethodDecHead(
                                [Public, Static]
                              , None
                              , Int
                              , Id("MyPower")
                              , [Param([], Int, Id("x"))]
                              , None
                              )
                            , Block(
                                [ EscapeFromStm(
                                    [ ExprStm(
                                        Invoke(
                                          Method(MethodName(Id("PowerGen")))
                                        , [ ExprName(Id("thisCode"))
                                          , ExprName(Id("n"))
                                          , ExprName(MetaVar(Id("x")))
                                          ]
                                        )
                                      )
                                    ]
                                  )
                                ]
                              )
                            )
                          ]
                        )
                      )
                    )
                  )
                )
              ]
            )
          )
        , MethodDec(
            MethodDecHead(
              [Public, Static]
            , None
            , Void
            , Id("PowerGen")
            , [ Param(
                  []
                , ClassOrInterfaceType(TypeName(Id("CodeAttr")), None)
                , Id("thisCode")
                )
              , Param([], Int, Id("n"))
              , Param(
                  []
                , ClassOrInterfaceType(TypeName(Id("Variable")), None)
                , MetaVar(Id("x"))
                )
              ]
            , None
            )
          , Block(
              [ QuoteBlockStms(
                  [ LocalVarDecStm(LocalVarDec([], Int, [VarDec(Id("p"))]))
                  , ExprStm(Assign(ExprName(Id("p")), Lit(Deci("1"))))
                  , EscapeFromStm(
                      [ While(
                          Gt(ExprName(Id("n")), Lit(Deci("0")))
                        , Block(
                            [ If(
                                Eq(
                                  Remain(ExprName(Id("n")), Lit(Deci("2")))
                                , Lit(Deci("0"))
                                )
                              , Block(
                                  [ QuoteBlockStms(
                                      [ ExprStm(
                                          Assign(
                                            ExprName(Id("x"))
                                          , Mul(ExprName(Id("x")), ExprName(Id("x")))
                                          )
                                        )
                                      ]
                                    )
                                  , ExprStm(
                                      Assign(
                                        ExprName(Id("n"))
                                      , Div(ExprName(Id("n")), Lit(Deci("2")))
                                      )
                                    )
                                  ]
                                )
                              , Block(
                                  [ QuoteBlockStms(
                                      [ ExprStm(
                                          Assign(
                                            ExprName(Id("p"))
                                          , Mul(ExprName(Id("p")), ExprName(Id("x")))
                                          )
                                        )
                                      ]
                                    )
                                  , ExprStm(
                                      Assign(
                                        ExprName(Id("n"))
                                      , Minus(ExprName(Id("n")), Lit(Deci("1")))
                                      )
                                    )
                                  ]
                                )
                              )
                            ]
                          )
                        )
                      ]
                    )
                  , Return(Some(ExprName(Id("p"))))
                  ]
                )
              ]
            )
          )
        , MethodDec(
            MethodDecHead(
              [Public, Static]
            , None
            , Int
            , Id("Power")
            , [ Param([], Int, Id("n"))
              , Param([], Int, Id("x"))
              ]
            , None
            )
          , Block(
              [ LocalVarDecStm(LocalVarDec([], Int, [VarDec(Id("p"))]))
              , ExprStm(Assign(ExprName(Id("p")), Lit(Deci("1"))))
              , While(
                  Gt(ExprName(Id("n")), Lit(Deci("0")))
                , Block(
                    [ If(
                        Eq(
                          Remain(ExprName(Id("n")), Lit(Deci("2")))
                        , Lit(Deci("0"))
                        )
                      , Block(
                          [ ExprStm(
                              Assign(
                                ExprName(Id("x"))
                              , Mul(ExprName(Id("x")), ExprName(Id("x")))
                              )
                            )
                          , ExprStm(
                              Assign(
                                ExprName(Id("n"))
                              , Div(ExprName(Id("n")), Lit(Deci("2")))
                              )
                            )
                          ]
                        )
                      , Block(
                          [ ExprStm(
                              Assign(
                                ExprName(Id("p"))
                              , Mul(ExprName(Id("p")), ExprName(Id("x")))
                              )
                            )
                          , ExprStm(
                              Assign(
                                ExprName(Id("n"))
                              , Minus(ExprName(Id("n")), Lit(Deci("1")))
                              )
                            )
                          ]
                        )
                      )
                    ]
                  )
                )
              , Return(Some(ExprName(Id("p"))))
              ]
            )
          )
        ]
      )
    )
  , ClassDec(
      ClassDecHead(
        []
      , Id("ArrayClassLoader")
      , None
      , Some(SuperDec(ClassType(TypeName(Id("ClassLoader")), None)))
      , None
      )
    , ClassBody(
        [ MethodDec(
            MethodDecHead(
              [Public]
            , None
            , ClassOrInterfaceType(TypeName(Id("Class")), None)
            , Id("loadClass")
            , [ Param(
                  []
                , ClassOrInterfaceType(TypeName(Id("String")), None)
                , Id("name")
                )
              , Param([], ArrayType(Byte), Id("classFile"))
              ]
            , None
            )
          , Block(
              [ Return(
                  Some(
                    Invoke(
                      Method(MethodName(Id("defineClass")))
                    , [ ExprName(Id("name"))
                      , ExprName(Id("classFile"))
                      , Lit(Deci("0"))
                      , ExprName(AmbName(Id("classFile")), Id("length"))
                      ]
                    )
                  )
                )
              ]
            )
          )
        ]
      )
    )
  ]
)
