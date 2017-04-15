module Main exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import WebSocket
import Table
import Json.Decode as Decode exposing(..)
import Json.Encode as Encode exposing(..)
import Basics
import Regex

import Bootstrap.Grid as Grid
import Bootstrap.Grid.Col as Col
import Bootstrap.Grid.Row as Row

main =
  Html.program
    { init = init
    , view = view
    , update = update
    , subscriptions = subscriptions
    }


-- MODEL
type alias Reservation =
  { darlehen : String
  , name : String
  }

type alias Model =
  { darlehen : String
  , name : String
  , tableState : Table.State
  , reservations : List Reservation
  , error : String
  , data : String
  }

type Msg
  = InputDarlehen String
  | InputName String
  | Send
  | NewMessage String
  | UpdateMessage String
  | SetTableState Table.State


init : (Model, Cmd Msg)
init =
  (Model "" "" (Table.initialSort "Darlehen") [] "" "waiting for update...", Cmd.none)

reservationDecoder : Decode.Decoder Reservation
reservationDecoder =
    Decode.map2 Reservation
        (Decode.at [ "darlehen" ] Decode.string)
        (Decode.at [ "name" ] Decode.string)

decodeReservation : String -> Result String Reservation
decodeReservation str =
    Decode.decodeString reservationDecoder str

encodeReservation : Reservation -> Encode.Value
encodeReservation {darlehen, name} =
    Encode.object
        [ ("darlehen", Encode.string darlehen)
        , ("name", Encode.string name)
        ]

-- UPDATE

validateDarlehen : String -> Bool
validateDarlehen =
  ((==) 0) << String.length << Regex.replace Regex.All (Regex.regex "[0-9]{8}") (\_ -> "")

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =

  case msg of

    InputDarlehen darlehen ->
        if validateDarlehen darlehen
        then ({model | darlehen = darlehen, error = ""}, Cmd.none)
        else
          let errMsg = case darlehen of
                          "" -> ""
                          _  -> "ungültige Darlehensnummer"
          in ({model | darlehen = darlehen, error = errMsg}, Cmd.none)

    InputName name ->
        ({model | name = name}, Cmd.none)

    Send ->
        let res = encodeReservation (Reservation model.darlehen model.name)
        in ({model | darlehen = "", name = ""}, WebSocket.send "ws://localhost:8080/drs" (Encode.encode 2 res))

    UpdateMessage data ->
        ({model | data = data}, Cmd.none)

    NewMessage message ->
        case decodeReservation message of
            Ok reservation -> ({model | reservations = reservation :: model.reservations} , Cmd.none)
            Err err        -> ({model | error = err}, Cmd.none)

    SetTableState newState ->
      ( {model | tableState = newState}, Cmd.none )


-- SUBSCRIPTIONS

subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.batch
    [
      WebSocket.listen "ws://localhost:8080/drs" NewMessage
    , WebSocket.listen "ws://localhost:8080/update" UpdateMessage
    ]


-- VIEW

invalidInput : Model -> Bool
invalidInput { darlehen, name, error } =
  error /= "" || darlehen == "" || name == ""


view : Model -> Html Msg
view model =
  Grid.container []
    [ Grid.row []
      [ Grid.col [ Col.xs12, Col.mdAuto ]
        [ input [placeholder "Darlehen", onInput InputDarlehen, Html.Attributes.value model.darlehen] []
        , input [placeholder "Name", onInput InputName, Html.Attributes.value model.name] []
        , button [onClick Send, disabled (invalidInput model)] [text "Ok"]
        ]
      ]
    , Grid.row []
      [ Grid.col [ Col.xs12, Col.mdAuto ]
        [ div [ style [ ("color", "red") ], class "small" ]
          [ text model.error ]
        ]
      ]
    , Grid.row []
      [ Grid.col [ Col.xs12, Col.mdAuto ]
        [ div []
          [ label [] [ text "Reservierungen" ]
          , Table.view config model.tableState model.reservations ]
          ]
      ]
    , Grid.row []
      [ Grid.col [ Col.xs12, Col.mdAuto ]
        [ div []
          [ text model.data ]
          ]
      ]
    ]

config : Table.Config Reservation Msg
config =
  Table.config
    { toId = .darlehen
    , toMsg = SetTableState
    , columns =
        [ Table.stringColumn "Darlehen" .darlehen
        , Table.stringColumn "Name" .name
        ]
    }
