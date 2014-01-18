
// on ready function
$(function () { 

  // a list of all of the available games
  var games;
  // all of the requests from the JSON file
  var requests;


  // get the JSON file
  $.getJSON('/server-REST.json', function(restRequests) {
    requests = restRequests;
    $.each(restRequests, function(index, request) {
      $('#actions-select').append('<option index=' + index + '>' + request.url + '</option>');
    });
  });


  // join a game
  $('#join-btn').on('click', function () {
    var postParams = {};
    var joinParams = {};
    joinParams['id'] = $('#game-select option:selected').first().attr('gameId');
    joinParams['color'] = 'blue';
    postParams['username'] = $('#user-select option:selected').first().val();
    postParams['password'] = $('#password').val();

    $.post('/user/login', postParams, function() {
      console.log('logged in');
      $.post('/games/join', joinParams, function(data) {
        console.log('game joined');
      }).fail(function() {
        console.log('failed to join a game');
      });
    }).fail(function() {
      console.log('failed to login');
    });// $.param(postParams); // $('#formI').serialize();
  });


  // make request to server
  $('#request-btn').on('click', function() {
    var actionIndex = $('#actions-select option:selected').first().attr('index');
    action = $('#actions-select option:selected').val();
    request = requests[actionIndex];

    if (request.method == 'POST') {
      if (request.type == 'FORM') { // get all of the form values
        var params = {};
        $('.param').each(function(index, param) { 
          params[$(param).attr('id')] = $(param).val();
        });

        $.post(action, params, function (data) {
          console.log(action + ' request succeeded');
          $('#json-response').val(JSON.stringify(data)).removeClass('hide');
        }).fail(function() {
          console.log(action + ' request failed');
        });

      } else { // just get the json-template
        params = $('#json-template').val();

        $.ajax({
          type: "POST",
          contentType: "application/json", 
          url: action,
          data: params,
          dataType: "json"
        }).done(function(data) {
          console.log(action + ' request succeeded');
          $('#json-response').val(JSON.stringify(data)).removeClass('hide');
        }).fail(function() {
          console.log(action + ' request failed');
        });
      }

    } else {
      $.get(action, function(data, status, jqxhr) {
        console.log(action + ' request succeeded');
        $('#json-response').val(JSON.stringify(data)).removeClass('hide');
      });
    }
  });


  // gets a list of games
  $.get('/games/list', function(data, status, jqxhr) {
    games = data;
    $.each(games, function(index, game) {
      $('#game-select').append('<option gameId='+game.id+'>' + game.title + '</option>');
    });
    populateUsers();
  });


  // handles game select option changed
  $('#game-select').on('change', function() {
    populateUsers(this);
  });


  // handles actions select option changed
  $('#actions-select').on('change', function() {
    var actionIndex = $('#actions-select option:selected').first().attr('index');
    request = requests[actionIndex];

    // hide the controls
    $('#json-template').addClass("hide");
    $('#json-response').addClass("hide");
    $('#request-btn').addClass("hide").removeClass("block");

    // populate description and method
    $('#description-lbl').text('description: ' + request.description);
    $('#method-lbl').text('method: ' + request.method);

    // clear the previous form
    $('#request-form').empty();

    if (request && request.method == 'POST') {
      if (request.type == 'FORM') {
        populatePostWithForm(request);
      } else {
        populatePostWithJSON(request);
      }
    }
    $('#request-btn').addClass("block").removeClass("hide");
  });


  // creates a form based on the arguments
  function populatePostWithForm(request) {
    $('#arguments-lbl').text('Arguments:');
    $('#request.form').append('<label id="arguments-lbl">Arguments: </label>');

    $.each(request.args, function(index, arg) {
      switch (arg.type) {
        case 'BOOLEAN':
          console.log(templateBuilder($('#boolean-template').html()));
          $('#request-form').append(templateBuilder($('#boolean-template')
              .html(), {'arg-name':arg.name, 'description':arg.description}));
          break;
        case 'STRING':
          $('#request-form').append(templateBuilder($('#string-template')
              .html(), {'arg-name':arg.name, 'description':arg.description}));
          break;
        case 'ENUMERATION':
          $('#request-form').append(templateBuilder($('#enumeration-template')
              .html(), {'arg-name':arg.name, 'description':arg.description}));

          $.each(arg.values, function(index, option) {
            console.log(option);
            $('#'+arg.name).append(templateBuilder($('#option-template')
                .html(), {'arg-name':option}))});
          break;
        case 'INTEGER':
          $('#request-form').append(templateBuilder($('#integer-template')
              .html(), {'arg-name':arg.name, 'description':arg.description}));
          break;
      }
    });
  }


  // takes in html with '{{example}}' encoded parameters and 
  // replaces them with a given value
  var templateBuilder = function(html, data) {
    for (name in data) {
      html = html.replace(new RegExp("{{" + name + "}}", "g"), data[name]);
    }
    return html;
  };


  // sets the template text and makes the controls visible
  function populatePostWithJSON(request) {
    $('#json-template').val(JSON.stringify(request.template, null, '\t')).removeClass("hide");
  }


  // populates the users select
  function populateUsers(select) {
    var gameId = $('#game-select option:selected').first().attr('gameId');
    var select = $('#user-select');
    select.empty();

    $.each(games[gameId].players, function(index, user) {
      $('#user-select').append('<option userId='+user.id+'>' + user.name+ '</option>');
    });
    // setGameCookie(gameId, $(select).val());
  }


  // sets game cookies
  function setGameCookie(key, value) {
    var cookie = '{"key":' + key + ', "value":' + value + '}';
    Cookies.set('reed.game', encodeURI(cookie));
  }

});
