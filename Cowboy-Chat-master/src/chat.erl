
-module(chat).
-behaviour(application).

%% application callbacks
-export([start/2]).
-export([stop/1]).

%%%=============================================================================
%%% application callbacks
%%%=============================================================================

start(_Type, _Args) ->
    {ok, _ControllerPid} = chat_controller_sup:start_link(),
    {ok, ServerPid} = chat_server_sup:start_link(), 
    io:fwrite("Chat server started successfully!~n"),
    {ok, ServerPid}.

stop(_State) ->
    ok = cowboy:stop_listener(my_http_listener).
 
