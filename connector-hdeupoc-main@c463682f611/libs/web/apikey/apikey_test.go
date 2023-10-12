package apikey_test

import (
	"fmt"
	"os"
	"testing"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/web/apikey"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestNew(t *testing.T) {
	testCases := []struct {
		name     string
		expected apikey.APIKey
		wantErr  bool
	}{
		{
			name: "test1",
			expected: apikey.APIKey{
				Name: "test1",
			},
			wantErr: false,
		},
		{
			name:     "",
			expected: apikey.APIKey{},
			wantErr:  true,
		},
	}

	for _, tt := range testCases {
		testName := fmt.Sprintf("When the name is %q", tt.name)
		t.Run(testName, func(t *testing.T) {
			ak, err := apikey.New(tt.name)
			if !tt.wantErr {
				assert.NoError(t, err)
				assert.Equal(t, tt.expected.Name, ak.Name)
				return
			}
			assert.Error(t, err)
			assert.Zero(t, ak)
		})
	}
}

func TestReadAll(t *testing.T) {
	root, err := os.Getwd()
	require.NoError(t, err)

	test := struct {
		path     string
		expected map[string]string
	}{
		path: root + "/test/readall.json",
		expected: map[string]string{
			"test": "4df3157e8cc6245aa4e68d0285affa341dd1bfea2a6fb17e94bca0db8258092d", // hash of : gBgfpl9TyNTVsMFA7CwLWepwd_pbPhoZ8ddXR3nCGsq6Eu4lmaIPp5698hwd
		},
	}

	codeByName, err := apikey.ReadAll(test.path)
	assert.NoError(t, err)
	assert.Equal(t, test.expected, codeByName)
}

func TestAdd(t *testing.T) {
	root, err := os.Getwd()
	require.NoError(t, err)

	testCases := []struct {
		path   string
		apiKey apikey.APIKey
		hash   string
	}{
		{
			path: root + "/test/add.json",
			apiKey: apikey.APIKey{
				Name: "test",
				Code: "Qw0468T1kkJmQ52HpCUL5hPhREn5Wad6NAiur_y_0fb7ODxFxE47vrrkFvRt",
			},
		},
		{
			path: root + "/test/add.json",
			apiKey: apikey.APIKey{
				Name: "test",
				Code: "H0P0kep6JjY0Au5JwnjVHLGqTcFd8fED8dLaLfsMzGtFFe31n0rr2Q9bzOW3",
			},
		},
	}

	for i, tt := range testCases {
		err = apikey.Add(tt.apiKey, tt.path)
		assert.NoError(t, err)

		m, err := apikey.ReadAll(tt.path)
		require.NoError(t, err)

		testCases[i].hash = m[tt.apiKey.Name]
	}

	if testCases[0].hash == testCases[1].hash {
		assert.Fail(t, "hash key are the same")
	}
}
