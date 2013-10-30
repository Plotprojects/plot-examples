/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var categories = ["books", "fashion", "mobile"];
var settingsKey = "categories";
var plotPublicKey = "REPLACE_ME";

var app = {
  // Application Constructor
  initialize: function() {
    app.bindEvents();
    app.loadSettings();
  },
  getSelectedCategories: function() {
    var settings = localStorage.getItem(settingsKey);
    if (settings) {
      return JSON.parse(settings);
    }
    return [];
  },
  setSelectedCategories: function(categories) {
    localStorage.setItem(settingsKey, JSON.stringify(categories));
  },
  loadSettings: function() {
    var selectedCategories = app.getSelectedCategories();
    selectedCategories.forEach(function (cat) {
      document.getElementById(cat).checked = true;
    });
  },
  updateSettings: function() {
    var selectedCategories = categories.filter(function (cat) {
      return document.getElementById(cat).checked;
    });
    app.setSelectedCategories(selectedCategories);
  },
  // Bind Event Listeners
  //
  // Bind any events that are required on startup. Common events are:
  // 'load', 'deviceready', 'offline', and 'online'.
  bindEvents: function() {
    document.addEventListener('deviceready', this.onDeviceReady, false);

    categories.forEach(function (cat) {
      document.getElementById(cat).addEventListener("click", app.updateSettings);
    });
  },
  // deviceready Event Handler
  //
  // The scope of 'this' is the event. In order to call the 'receivedEvent'
  // function, we must explicity call 'app.receivedEvent(...);'
  onDeviceReady: function() {
    app.receivedEvent('deviceready');
  },
  // Update DOM on a Received Event
  receivedEvent: function(id) {
    if (id == 'deviceready') {
      app.loadPlot();
    }
  },
  loadPlot: function() {
    var plot = cordova.require("cordova/plugin/plot");
    var config = plot.exampleConfiguration;
    config.publicKey = plotPublicKey; //put your public key here

    plot.filterCallback = function(notifications) {
      var selectedCategories = app.getSelectedCategories();
      return notifications.filter(function (notification) {
        try {
          var data = JSON.parse(notification.data);
          return selectedCategories.indexOf(data.category) !== -1;
        } catch (e) {
          console.log(e.message);
          return false;
        }
      });
    };

    plot.notificationHandler = function(notification, notificationData) {
      var data = JSON.parse(notificationData);
      document.getElementById("couponinfo").textContent = data.info;
    };

    plot.init(config);
  }
};
