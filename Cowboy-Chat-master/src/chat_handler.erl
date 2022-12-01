-module(chat_handler).
-export([init/2]).
-export([websocket_init/1]).
-export([websocket_handle/2]).
-export([websocket_info/2]).
-export([terminate/3]).

%% handler websocket https://ninenines.eu/docs/en/cowboy/2.8/guide/handlers/
init(Req, Opts) ->
	{cowboy_websocket, Req, Opts}.


websocket_init(State) ->
	self() ! {send_message, self(), "Sei entrato: inserisci l'id"},
  {[], State}. 


websocket_handle({text, Msg}, State) ->
  Details=string:lexemes(Msg, "-->"), % msg
  
  
  Param2=lists:last(Details),
  Param1=hd(Details),
  if 
    Param1 == <<"!PING">> ->
      %%Command=Param1,
      S=State, 
      ok;
    Param1 == <<"!ENTER">> ->
      %%Command=Param1,
      NickSender= Param2,
      S=NickSender,
      chat_server:enter({self(),NickSender});
    Param1 == <<"!EXIT">> ->
      %%Command=Param1, 
      S=State,
      chat_server:leave(self());
    true ->
      Message=Param1,
      NickReceiver=Param2, 
      S=State,
      chat_server:send_message({self(),{NickReceiver,S}},Message)
  end,
  {ok, S};
websocket_handle(_Data, State) ->
  {[], State}.
  
websocket_info({send_message, _ServerPid, Msg}, State) ->
    {reply, {text, Msg}, State};
websocket_info(_Info, State) ->
  {[], State}.

 
 terminate(_Reason, _Req, _State) ->
    chat_server:leave(self()),
    ok.

