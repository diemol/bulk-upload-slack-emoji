### Quick script using Selenium WebDriver that uploads emojis to a Slack workspace 

Before running the script, you should configure Chrome and ChromeDriver to execute the WebDriver script.
More information can be seen here: http://chromedriver.chromium.org/getting-started


1. Go to a page like https://slackmojis.com/, and download the ones you'd like into a separate directory.
1. Set the following environment variables:
    1. `SLACK_EMAIL`: Email you use to log into Slack
    1. `SLACK_PASSWORD`: Your Slack password
    1. `SLACK_WORKSPACE`: Name of your workspace. If your workspace is https://ctm-online.slack.com, then the value for this is `ctm-online`.
    1. `SLACK_EMOJI_PATH`: Full path of the local directory where the emoji files are.
1. Execute the script

