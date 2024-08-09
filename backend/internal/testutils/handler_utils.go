package testutils

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
)

// CreateRequest creates a new HTTP request with the given method, URL, and body.
func CreateRequest(method, url string, body interface{}) (*http.Request, *httptest.ResponseRecorder) {
	jsonData, err := json.Marshal(body)
	if err != nil {
		panic(err)
	}
	req, err := http.NewRequest(method, url, bytes.NewBuffer(jsonData))
	if err != nil {
		panic(err)
	}
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	return req, w
}
