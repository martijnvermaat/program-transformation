[
   Pred                    -- _1,
   Pred                    -- H hs=0 [_1 "(" _2 ")"],
   Varlist                 -- _1,
   Varlist.1:iter-star-sep -- H hs=0 [_1 ", "],
   Constant                -- KW[_1],
   Not                     -- H hs=1 [KW["not"] _1],
   And                     -- H hs=1 [_1 KW["/\\"] _2],
   Or                      -- H hs=1 [_1 KW["\\/"] _2],
   Equiv                   -- H hs=1 [_1 KW["<->"] _2],
   Forall                  -- V is=2 [
                                H hs=1 [KW ["forall"] H hs=0 [_1 ":"]]
                                _2],
   Exists                  -- V is=2 [
                                H hs=1 [KW ["exists"] H hs=0 [_1 ":"]]
                                _2],
   Parenthetical           -- H hs=0 ["(" _1 ")"]
]
