package com.example.simplejetpackapp.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplejetpackapp.R
import com.example.simplejetpackapp.ui.components.DividerWithText
import com.example.simplejetpackapp.ui.components.LoginTextField
import com.example.simplejetpackapp.ui.components.PasswordTextField
import com.example.simplejetpackapp.ui.components.SocialSignInButton
import com.example.simplejetpackapp.ui.theme.SimpleJetpackAppTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    // Observe state from the ViewModel
    val email = viewModel.emailInput
    val password = viewModel.passwordInput
    val isLoading = viewModel.isLoading
    val loginError = viewModel.loginError

    // Used to open the "Sign Up" link
    val uriHandler = LocalUriHandler.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply system padding (for status bar, etc.)
                .padding(horizontal = 24.dp) // Apply consistent horizontal padding
                .verticalScroll(rememberScrollState()), // Make the column scrollable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Align content to the top
        ) {

            Spacer(modifier = Modifier.height(64.dp)) // Space from top

            // --- HEADER ---
            Text(
                text = "Welcome back!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Log in to your account",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- FORM FIELDS ---
            LoginTextField(
                value = email,
                onValueChange = { viewModel.emailInput = it },
                label = "Email or Username",
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = loginError != null,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = password,
                onValueChange = { viewModel.passwordInput = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                isError = loginError != null,
                enabled = !isLoading
            )

            // Show error message if it exists
            if (loginError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = loginError ?: "An unknown error occurred.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- FORGOT PASSWORD ---
            TextButton(
                onClick = { /* TODO: Handle Forgot Password navigation */ },
                modifier = Modifier.align(Alignment.End),
                enabled = !isLoading
            ) {
                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- LOGIN BUTTON ---
            Button(
                onClick = { viewModel.attemptLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- DIVIDER ---
            DividerWithText(text = "OR")

            Spacer(modifier = Modifier.height(32.dp))

            // --- SOCIAL LOGINS ---
            // These require the drawable files (ic_google_logo.xml, ic_microsoft_logo.xml)
            SocialSignInButton(
                text = "Continue with Google",
                icon = painterResource(id = R.drawable.ic_google_logo),
                onClick = { /* TODO: Handle Google Sign In */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SocialSignInButton(
                text = "Continue with Microsoft",
                icon = painterResource(id = R.drawable.ic_microsoft_logo),
                onClick = { /* TODO: Handle Microsoft Sign In */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- SIGN UP LINK ---
            SignUpLink(
                onSignUpClicked = {
                    // This will open a browser, you can change this to navigate
                    // to a new screen later.
                    try {
                        uriHandler.openUri("https://google.com/search?q=sign+up")
                    } catch (e: Exception) {
                        // Handle error (e.g., no browser)
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp)) // Bottom padding
        }
    }
}

/**
 * Creates the "Don't have an account? Sign Up" clickable text.
 */
@Composable
private fun SignUpLink(
    onSignUpClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Create a rich text string
    val annotatedString = buildAnnotatedString {
        // "Don't have an account? " (normal text)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
            append("Don't have an account? ")
        }

        // "Sign Up" (clickable text)
        // Attach a "tag" to this part of the string
        pushStringAnnotation(tag = "SIGN_UP", annotation = "sign_up")
        withStyle(style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )) {
            append("Sign Up")
        }
        pop() // Stop applying the tag
    }

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium,
        onClick = { offset ->
            // Check if the user clicked on the "SIGN_UP" part
            annotatedString.getStringAnnotations(tag = "SIGN_UP", start = offset, end = offset)
                .firstOrNull()?.let {
                    // If so, trigger the callback
                    onSignUpClicked()
                }
        },
        modifier = modifier
    )
}

// --- PREVIEWS ---

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun LoginScreenPreview() {
    SimpleJetpackAppTheme {
        // We use a dummy ViewModel for the preview
        val previewViewModel = viewModel<LoginViewModel>()
        LoginScreen(viewModel = previewViewModel)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenDarkPreview() {
    SimpleJetpackAppTheme(darkTheme = true) {
        val previewViewModel = viewModel<LoginViewModel>()
        previewViewModel.loginError = "This is a preview error" // Test error state
        LoginScreen(viewModel = previewViewModel)
    }
}

