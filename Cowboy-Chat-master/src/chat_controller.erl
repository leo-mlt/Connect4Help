
-module(chat_controller).
-behaviour(gen_server).

%% API
-export([start/0]).

%% gen_server callbacks
-export([init/1, 
         handle_call/3, 
         handle_cast/2, 
         handle_info/2, 
         terminate/2]).

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



handle_cast({private_message, Pid_Sender, {Id_Dest,Id_Sender}, Msg}, Users) ->
    private_message({Id_Dest,Id_Sender}, Pid_Sender, Msg, Users),
    {noreply, Users};



handle_cast(_Message, State) ->
    {noreply, State}.

handle_info(_Message, State) -> 
    {noreply, State}.

terminate(_Reason, _State) -> 
    ok.


 
%%%=============================================================================
%%% Internal functions
%%%=============================================================================



private_message({Id_Dest,Id_Sender}, Pid_Sender, Msg, Users) ->
    FormatMsg = format_message(Id_Sender, Msg),
    case ets:lookup(Users, Id_Dest) of
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
    FormattedMsg = concat([format_time(), "^", Nick, "^", Msg, "\n"]),
    FormattedMsg.

format_time() ->
    {Mega, Sec, Micro} = os:timestamp(),
    io_lib:format('(~13..0b)', [(Mega*1000000 + Sec)*1000 + round(Micro/1000)]).

to_string(Value) when is_binary(Value) -> binary_to_list(Value);
to_string(Value) -> Value.
concat(List) ->
  lists:flatten(lists:map(fun to_string/1, List)).