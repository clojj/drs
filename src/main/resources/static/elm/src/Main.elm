module Main exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import WebSocket
import Table
import Json.Decode as Decode exposing(..)
import Json.Encode as Encode exposing(..)
import Basics

main =
  Html.program
    { init = init
    , view = view
    , update = update
    , subscriptions = subscriptions
    }


-- MODEL
type alias Reservation =
  { darlehen : Int
  }

type alias Model =
  { darlehen : Int
  , tableState : Table.State
  , reservations : List Reservation
  , error : String
  }


init : (Model, Cmd Msg)
init =
  (Model 0 (Table.initialSort "Darlehen") [] "", Cmd.none)

reservationDecoder : Decode.Decoder Reservation
reservationDecoder =
    Decode.map Reservation (Decode.at [ "darlehen" ] Decode.int)

decodeReservation : String -> Result String Reservation
decodeReservation str =
    Decode.decodeString reservationDecoder str

encodeReservation : Reservation -> Encode.Value
encodeReservation reservation =
    Encode.object
        [ ("darlehen", Encode.int reservation.darlehen)
        ]

-- UPDATE

type Msg
  = Input String
  | Send
  | NewMessage String
  | SetTableState Table.State


update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    Input newInput ->
        case String.toInt newInput of
            Ok n    ->  ({model | darlehen = n, error = ""}, Cmd.none)
            Err err ->  ({model | error = err}, Cmd.none)

    Send ->
        let res = encodeReservation (Reservation model.darlehen)
        in (model, WebSocket.send "ws://localhost:8080/drs" (Encode.encode 2 res))

    NewMessage message ->
        case decodeReservation message of
            Ok reservation -> ({model | reservations = reservation :: model.reservations} , Cmd.none)
            Err err        -> ({model | error = err}, Cmd.none)

    SetTableState newState ->
      ( {model | tableState = newState}, Cmd.none )


-- SUBSCRIPTIONS

subscriptions : Model -> Sub Msg
subscriptions model =
  WebSocket.listen "ws://localhost:8080/drs" NewMessage


-- VIEW

view : Model -> Html Msg
view model =
  div []
    [ input [placeholder "Darlehen", onInput Input] []
    , button [onClick Send] [text "Reservierung"]
    , Table.view config model.tableState model.reservations
    , div [] [text model.error]
    ]


config : Table.Config Reservation Msg
config =
  Table.config
    { toId =  toString << .darlehen
    , toMsg = SetTableState
    , columns =
        [ Table.intColumn "Darlehen" .darlehen
        ]
    }
