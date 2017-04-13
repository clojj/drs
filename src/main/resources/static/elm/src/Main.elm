module Main exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import WebSocket
import Table
import Json.Decode as Decode exposing(..)

main =
  Html.program
    { init = init
    , view = view
    , update = update
    , subscriptions = subscriptions
    }


-- MODEL
type alias Message =
  { msg : String
  }

type alias Model =
  { input : String
  , tableState : Table.State
  , messages : List Message
  }


init : (Model, Cmd Msg)
init =
  (Model "" (Table.initialSort "Message") [], Cmd.none)

messageDecoder : Decode.Decoder Message
messageDecoder =
    Decode.map Message (Decode.at [ "msg" ] Decode.string)

-- UPDATE

type Msg
  = Input String
  | Send
  | NewMessage String
  | SetTableState Table.State


update : Msg -> Model -> (Model, Cmd Msg)
update msg {input, tableState, messages} =
  case msg of
    Input newInput ->
      (Model newInput tableState messages , Cmd.none)

    Send ->
      (Model "" tableState messages, WebSocket.send "ws://localhost:8080/drs" input)

    NewMessage str ->
      (Model input tableState ((Message str) :: messages), Cmd.none)

    SetTableState newState ->
      ( { input = input, tableState = newState, messages = messages } , Cmd.none )

-- SUBSCRIPTIONS

subscriptions : Model -> Sub Msg
subscriptions model =
  WebSocket.listen "ws://localhost:8080/drs" NewMessage


-- VIEW

view : Model -> Html Msg
view model =
  div []
    [ input [placeholder "Name", onInput Input] []
    , button [onClick Send] [text "Reservierung"]
    , Table.view config model.tableState model.messages
    ]


config : Table.Config Message Msg
config =
  Table.config
    { toId = .msg
    , toMsg = SetTableState
    , columns =
        [ Table.stringColumn "Message" .msg
        ]
    }
