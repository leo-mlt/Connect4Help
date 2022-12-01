%%%-----------------------------------------------------------------------------
%%% @author Amin
%%% @copyright 2015 Free Software
%%% @doc Server is responsible to accept connections from clients and manage the    
%%%      chat sessions.                                                             
%%%      It works with controller process to keep track of users who join and 
%%%      leave chat server.                                                               
%%% @end
%%%-----------------------------------------------------------------------------
-module(chat_server).
-behaviour(gen_server).

%% API
-export([start_link/0]).
-export([enter/1, leave/1, send_message/2]).
%% gen_server callbacks
-export([init/1, 
         handle_call/3, 
         handle_cast/2, 
         handle_info/2, 
         terminate/2, 
         code_change/3]).

-include("chat.hrl").

-record(state, {name, % client's name
                next,
                pid}). % the current pid

%%%=============================================================================
%%% API
%%%=============================================================================



enter({Pid, Id}) ->
    io:format("~p Joine! ~n", [Pid]),
    gen_server:cast(?SERVER, {enter,{Pid,Id}}).

leave(Pid) ->
    io:format("~p leave! ~n", [Pid]),
    gen_server:cast(?SERVER, {leave, Pid}).

send_message({Pid_Sender,Id_Dest}, Message) ->
    gen_server:cast(?SERVER, {send_message, {Pid_Sender,Id_Dest}, Message}).




%%------------------------------------------------------------------------------
%% @doc Starts chat server.
%% 
%% @spec start_link(Socket::socket()) -> {ok, Pid}
%% where 
%%  Pid = pid()   
%% @end
%%------------------------------------------------------------------------------
start_link() ->
    gen_server:start_link({local, ?MODULE}, ?MODULE, [], []).

%%%=============================================================================
%%% gen_server callbacks
%%%=============================================================================

init(_) -> 
    Dispatch = cowboy_router:compile([
    {'_', [{'_', chat_handler, #{}}]}
    ]),
    cowboy:start_clear(my_http_listener,
			  [{port, 8080}],
			  #{env=> #{dispatch =>Dispatch}}
             ),
    {ok, #state{}}.

handle_call({leave, Pid}, _From, S) ->
    io:format("~p leave_cast! ~n",[Pid]),
    quit(Pid, S),
    {noreply, S}.

%%%%funzioni per chat%%%%%%%

handle_cast({leave, Pid}, S) ->
    io:format("~p leave_cast! ~n",[Pid]),
    quit(Pid, S),
    {noreply, S};
handle_cast({enter,{Pid,Id}}, S ) ->
    Reply = set_nick(Pid, Id, S),
    Reply;
handle_cast({send_message, {Pid_Sender,Id_Dest}, Message},  S) ->
    send_private_msg(Pid_Sender,Id_Dest, Message),
    {noreply, S}.




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



handle_info(_Info, State) ->
    {noreply, State}.

terminate(_Reason, _State) ->
    {noreply, _State}.

code_change(_OldVsn, State, _Extra) ->
    {ok, State}.

%%%=============================================================================
%%% Internal functions
%%%=============================================================================



set_nick(Pid, Nick, S) ->
    Response = gen_server:call(?CONTROLLER, {check_nick, Nick, Pid}),
    case Response of 
        nick_in_use ->
            send(Pid,"Nick in use",[]),
            {noreply, S#state{pid=Pid, next=nick}};    
        ok->
            send(Pid, "~p Welcome in the chat!", [Nick]),
            {noreply, S#state{pid=Pid, name=Nick, next=chat}}
    end.
send_private_msg(Pid_Sender, Nick_Reciver, Msg) ->
    gen_server:cast(?CONTROLLER, {private_message, Pid_Sender, Nick_Reciver, 
                                            Msg}).
quit(Pid, _S) ->
    send(Pid, "Goodbye ~p", [Pid]),
    io:format("~p Left the chat server! ~n", [Pid]),
    gen_server:call(?CONTROLLER, {disconnect, Pid}).
    %togliere dalla tabella l'utente

%%%=============================================================================
%%% Helper functions
%%%=============================================================================


send(Pid, Str, Args) ->
    io:format("~p send! ~n",[Str]),
    Pid ! {send_message, self(), io_lib:format(Str++"~n", Args)},
    ok.

 
