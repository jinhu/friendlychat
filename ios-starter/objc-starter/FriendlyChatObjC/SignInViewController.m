//
//  Copyright (c) 2016 Google Inc.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

#import "AppState.h"
#import "Constants.h"
#import "MeasurementHelper.h"
#import "SignInViewController.h"

@import FirebaseAuth;

@interface SignInViewController ()
@property (weak, nonatomic) IBOutlet UITextField *emailField;
@property (weak, nonatomic) IBOutlet UITextField *passwordField;
@end

@implementation SignInViewController

- (void)viewDidAppear:(BOOL)animated {
}

- (IBAction)didTapSignIn:(id)sender {
  [self signedIn:nil];
}

- (IBAction)didTapSignUp:(id)sender {
  [self setDisplayName:nil];
}

- (void)setDisplayName:(FIRUser *)user {
  [self signedIn:nil];
}

- (IBAction)didRequestPasswordReset:(id)sender {
}

- (void)signedIn:(FIRUser *)user {
  [MeasurementHelper sendLoginEvent];

  [AppState sharedInstance].signedIn = YES;
  [[NSNotificationCenter defaultCenter] postNotificationName:NotificationKeysSignedIn
                                                      object:nil userInfo:nil];
  [self performSegueWithIdentifier:SeguesSignInToFp sender:nil];
}

@end
