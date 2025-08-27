from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher
from textblob import TextBlob

class ActionAnalyzeSentiment(Action):

    def name(self):
        return "action_analyze_sentiment"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: dict):

        last_message = tracker.latest_message.get('text')
        blob = TextBlob(last_message)
        sentiment_score = blob.sentiment.polarity  # -1 to 1

        if sentiment_score > 0.1:
            dispatcher.utter_message(response="utter_happy")
        elif sentiment_score < -0.1:
            dispatcher.utter_message(response="utter_cheer_up")
        else:
            dispatcher.utter_message(response="utter_did_that_help")

        return []
