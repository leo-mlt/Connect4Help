
-module(chat_controller_sup).
-behaviour(supervisor).

%% API
-export([start_link/0]).

%% supervisor callback
-export([init/1]).

-include("chat.hrl").
%%%=============================================================================
%%% API
%%%=============================================================================

%%------------------------------------------------------------------------------
%% @doc Starts the controller process and supervise it.
%% @spec start_link() -> {ok, Pid}
%% where
%%  Pid = pid()
%% @end
%%------------------------------------------------------------------------------
% {local, ?Module} specifies that the supervisor is locally registered whith name ?MODULE
%  ?MODULE is the name of the callback module, that is the module
%  where the callback functions are located
start_link() ->
    supervisor:start_link({local, ?MODULE}, ?MODULE, []).

%%%=============================================================================
%%% supervisor callbacks
%%%=============================================================================

init([]) ->

    ControllerSpec =  {?CONTROLLER,
                       {?CONTROLLER, start, []},
                       permanent, 1000, worker, [?CONTROLLER]},

    ChildSpecs = [ControllerSpec],

    ok = supervisor:check_childspecs(ChildSpecs),
    StartSpecs = {{one_for_one, 60, 3600}, ChildSpecs},
    {ok, StartSpecs}.



%% we have used one_for_one strategy: if a child terminates,
%% only that process is restarted

%% the supervisors have a built-in mechanism to limit the number
%% of restarts which can occur in a given time interval
%% for this we use intensity: 60 and period: 3600
%% if more than 60 number of restart occur in the 3600
%% seconds, the supervisor terminates all the child processes
%% and then itself

%% id is used to identify the child specification internally
%% by the supervisor

%% start defines the function call used to start the child process

%% restart defines when terminated child process is to be
%% restarted, a permanent child process is always restarted

%% shutdown defines how a child process is to be terminated