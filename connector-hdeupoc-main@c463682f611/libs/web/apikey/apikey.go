// Package apikey helps us to manage an API key.
package apikey

import (
	"crypto/rand"
	"crypto/sha256"
	"encoding/base64"
	"encoding/json"
	"errors"
	"fmt"
	"os"
)

// An APIKey represents an API name and its code.
type APIKey struct {
	Name string
	Code string
}

// New is a factory function to generate a random API code with a given name.
func New(name string) (APIKey, error) {
	if len(name) == 0 {
		return APIKey{}, fmt.Errorf("api name is required")
	}

	length := 45
	b := make([]byte, length)

	_, err := rand.Read(b)
	if err != nil {
		return APIKey{}, fmt.Errorf("reading random: %w", err)
	}

	code := base64.URLEncoding.EncodeToString(b)

	return APIKey{
		Name: name,
		Code: code,
	}, nil
}

// ReadAll reads all the API keys from keys json file.
func ReadAll(path string) (map[string]string, error) {
	codeByName := make(map[string]string)

	b, err := os.ReadFile(path)
	if err != nil {
		if errors.Is(err, os.ErrNotExist) {
			return codeByName, nil
		}

		return nil, fmt.Errorf("reading file: %w", err)
	}

	// Return if the file is empty.
	if len(b) == 0 {
		return codeByName, nil
	}

	err = json.Unmarshal(b, &codeByName)
	if err != nil {
		return nil, fmt.Errorf("unmarshalling: %w", err)
	}

	return codeByName, nil
}

// Add adds a new API key on keys json file.
func Add(apiKey APIKey, path string) error {
	codeByName, err := ReadAll(path)
	if err != nil {
		return fmt.Errorf("reading all: %w", err)
	}

	h := hash(apiKey.Code)
	codeByName[apiKey.Name] = h

	b, err := json.Marshal(codeByName)
	if err != nil {
		return fmt.Errorf("marshalling: %w", err)
	}

	err = os.WriteFile(path, b, 0644)
	if err != nil {
		return fmt.Errorf("writing file: %w", err)
	}

	return nil
}

// hash hashes a code string and return it as a string.
func hash(code string) string {
	h := sha256.Sum256([]byte(code))

	return fmt.Sprintf("%x", h)
}

// Authenticate check if a given key is listed on a given keys map.
func Authenticate(key string, keys map[string]string) error {
	if key == "" {
		return errors.New("no key provided")
	}

	h := hash(key)
	for _, k := range keys {
		if k == h {
			return nil
		}
	}

	return errors.New("api key not valid")
}
