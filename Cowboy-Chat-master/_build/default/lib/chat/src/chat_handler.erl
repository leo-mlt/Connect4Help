-module(chat_handler).
-export([init/2]).
-export([websocket_init/1]).
-export([websocket_handle/2]).
-export([websocket_info/2]).
-export([terminate/3]).

init(Req, Opts) ->
	{cowboy_websocket, Req, Opts}.

websocket_init(State) ->
	self() ! {send_message, self(), "Sei entrato: inserisci l'id"},
  {[], State}. 


websocket_handle({text, Msg}, State) ->
  Details=string:lexemes(Msg, "-->"), % msg
  
  NickReciver=lists:last(Details),
  Msg_User=hd(Details),
  if 
    Msg_User == <<"!PING">> -> 
      ok;
    Msg_User == <<"!ENTER">> ->
      chat_server:enter({self(),NickReciver});
    Msg_User == <<"!EXIT">> -> 
      chat_server:leave(self());
    true -> 
      chat_server:send_message({self(),NickReciver},Msg_User)
  end,
  {ok, State};
websocket_handle(_Data, State) ->
  {[], State}.
  
websocket_info({send_message, _ServerPid, Msg}, State) ->
    {reply, {text, Msg}, State};
websocket_info({timeout, _Ref, Msg}, State) ->
	%erlang:start_timer(1000, self(), <<"How' you doin'?">>),
	{[{text, Msg}], State};
websocket_info(_Info, State) ->
  {[], State}.

 
 terminate(_Reason, _Req, _State) ->
   %problemi con invia messaggio.
    chat_server:leave(self()),
    ok.

