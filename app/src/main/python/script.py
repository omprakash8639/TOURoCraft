import requests as rq
def ai_model(quest):
    url='https://tourc.pagekite.me/'
    payload={'quest' : quest}
    response=rq.get(url,params=payload)
    json_values=response.json()
    return json_values