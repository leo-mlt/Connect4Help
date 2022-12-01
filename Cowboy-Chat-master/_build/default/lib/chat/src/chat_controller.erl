%%%-----------------------------------------------------------------------------
%%% @author Amin 
%%% @copyright 2015 Free software
%%% @doc Controller is responsible mainly for adding users to the data center 
%%%      and removing their name when they disconnect from the server.                
%%%      It does its job via getting requests from chat server.                   
%%%
%%% @end
%%%-----------------------------------------------------------------------------
-module(chat_controller).
-behaviour(gen_server).

%% API
-export([start/0]).

%% gen_server callbacks
-export([init/1, 
         handle_call/3, 
         handle_cast/2, 
         handle_info/2, 
         terminate/2,
         code_change/3]).

-include("chat.hrl").

%%%=============================================================================
%%% API
%%%=============================================================================

%%------------------------------------------------------------------------------
%% @doc Starts chat server controller.
%%
%% @spec start() -> {ok, Pid}
%% where 
%%  Pid = pid()
%% @end
%%------------------------------------------------------------------------------
start() ->
    gen_server:start_link({local, ?MODULE}, ?MODULE, [], []).

%%%=============================================================================
%%% gen_server callbacks
%%%=============================================================================

init([]) ->
    % ets table is used to keep track of online users (nicks)
    % and their associated pid.
    Users = ets:new(users,[set]),
    {ok, Users}.

handle_call({check_nick, Nick, Pid}, _From, Users) ->  
    Response = check_nick(Nick, Users, Pid),
    {reply, Response, Users};

handle_call({disconnect, Pid}, _From, Users) ->
    Response = disconnect_nick(Pid, Users),
    {reply, Response, Users};

handle_call(_Message, _From, State) ->
    {reply, error, State}.

handle_cast({say, Nick, Msg}, Users) ->
    broadcast(Nick, Msg, Users),
    {noreply, Users}; 

handle_cast({nick_list, Socket}, Users) ->
    nick_list(Socket, Users),
    {noreply, Users};

handle_cast({private_message, Pid_Sender, Nick_Recevier, Msg}, Users) ->
    private_message(Nick_Recevier, Pid_Sender, Msg, Users),
    {noreply, Users};

handle_cast({join, Nick}, Users) ->
    broadcast(Nick, "joined the chat! \n", Users),
    {noreply, Users};

handle_cast({left, Nick}, Users) ->
    broadcast(Nick, "left the chat! \n", Users),
    {noreply, Users};

handle_cast(_Message, State) ->
    {noreply, State}.

handle_info(_Message, State) -> 
    {noreply, State}.

terminate(_Reason, _State) -> 
    ok.

code_change(_OldVersion, State, _Extra) -> 
    {ok, State}.
 
%%%=============================================================================
%%% Internal functions
%%%=============================================================================

broadcast(Nick, Msg, Users) ->
    FormatMsg = format_message(Nick, Msg),
    Pids = [Pid || {N, Pid} <- ets:tab2list(Users), N =/= Nick],
    lists:foreach(fun(Pid) -> Pid ! {send_message, Pid, FormatMsg} end, Pids).

nick_list(Pid, Users) ->
    Nicks = [N ++ " " || {N, S} <- ets:tab2list(Users), S =/= Pid],
    Pid ! {send_message, Pid, "Online people: " ++ Nicks ++ "\n"}.

private_message(Nick_Reciver, Pid_Sender, Msg, Users) ->
    FormatMsg = format_message(Nick_Reciver, Msg),
    case ets:lookup(Users, Nick_Reciver) of
        [] -> 
            Pid_Sender ! {send_message, Pid_Sender,"Users not available."};
        [{_,Pid}] ->
            Pid ! {send_message, Pid_Sender, FormatMsg}
    end.

check_nick(Nick, Users, Pid) ->
    case ets:insert_new(Users, {Nick, Pid}) of
        true ->
            ok;
        false ->
            nick_in_use
    end.

disconnect_nick(Pid, Users) ->
    ets:match_delete(Users, {'_',Pid}),
    {ok, Users}.

%%%=============================================================================
%%% Helper functions
%%%=============================================================================

format_message(Nick, Msg) ->
    FormattedMsg = concat([format_time(), " ", Nick, ":", Msg, "\n"]),
    FormattedMsg.

format_time() ->
    {H, M, S} = time(),
    io_lib:format('(~2..0b:~2..0b:~2..0b)', [H, M, S]).

to_string(Value) when is_binary(Value) -> binary_to_list(Value);
to_string(Value) -> Value.
concat(List) ->
  lists:flatten(lists:map(fun to_string/1, List)).